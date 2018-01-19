package com.hyst.bus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.hyst.bus.R;
import com.hyst.bus.adapter.RecyclerAdapter;
import com.hyst.bus.constant.Constant;
import com.hyst.bus.model.RecyclerHolder;
import com.hyst.bus.model.event.SetPointEvent;
import com.hyst.bus.util.AMapUtil;
import com.hyst.bus.util.ToastUtil;
import com.hyst.bus.util.ViewUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/16.
 */

public class RoutePlanActivity extends BaseActivity implements RouteSearch.OnRouteSearchListener {
    private TextView tv_start;
    private TextView tv_end;
    private ImageView iv_back;
    private ImageView iv_exchange;
    //
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private List<BusPath> data;
    private BusRouteResult mBusRouteResult;
    //
    private SetPointEvent startPoint;
    private SetPointEvent endPoint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_route_plan;
    }

    @Override
    protected void initView() {
        tv_start = findViewById(R.id.tv_start);
        tv_end = findViewById(R.id.tv_end);
        tv_start.setOnClickListener(this);
        tv_end.setOnClickListener(this);
        recyclerView = findViewById(R.id.recyclerView);
        iv_exchange = findViewById(R.id.iv_exchange);
        iv_exchange.setOnClickListener(this);
        iv_back = findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        startPoint = getIntent().getParcelableExtra("startPoint");
        endPoint = getIntent().getParcelableExtra("endPoint");
        if (startPoint == null && endPoint == null) {
            return;
        }
        tv_start.setText(startPoint.getContent());
        tv_end.setText(endPoint.getContent());
        data = new ArrayList<>();
        adapter = new RecyclerAdapter<BusPath>(this, data, R.layout.item_route_plan) {
            @Override
            public void convert(RecyclerHolder holder, final BusPath path) {
                holder.setText(R.id.tv_name, AMapUtil.getBusPathTitle(path));
                holder.setText(R.id.tv_address, AMapUtil.getBusPathDes(path));
                holder.setOnClickListener(R.id.ll_point, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(RoutePlanActivity.this, RouteDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("path", path);
                        bundle.putParcelable("result", mBusRouteResult);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerView = ViewUtil.getVRowsNoLine(this, recyclerView, 1);
        recyclerView.setAdapter(adapter);
        searchRoute(startPoint.getLatLonPoint(), endPoint.getLatLonPoint());
    }

    /**
     * @param startPoint 起点
     * @param endPoint   终点
     */
    private void searchRoute(LatLonPoint startPoint, LatLonPoint endPoint) {
        RouteSearch routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint, endPoint);
        RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, RouteSearch.BusLeaseWalk, "西安市", 0);
        //开始规划路径
        routeSearch.calculateBusRouteAsyn(query);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = null;
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_exchange:
                if (startPoint != null && endPoint != null) {
                    SetPointEvent event = startPoint;
                    startPoint = endPoint;
                    endPoint = event;
                    tv_start.setText(startPoint.getContent());
                    tv_end.setText(endPoint.getContent());
                    searchRoute(startPoint.getLatLonPoint(), endPoint.getLatLonPoint());
                }
                break;
            case R.id.tv_start:
                intent = new Intent(this, SetPointActivity.class);
                intent.putExtra(Constant.POINT_TYPE, Constant.POINT_TYPE_START_VALUE);
                intent.putExtra(Constant.POINT_TAG, "RoutePlanActivity");
                startActivity(intent);
                break;
            case R.id.tv_end:
                intent = new Intent(this, SetPointActivity.class);
                intent.putExtra(Constant.POINT_TYPE, Constant.POINT_TYPE_END_VALUE);
                intent.putExtra(Constant.POINT_TAG, "RoutePlanActivity");
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onBusRouteSearched(BusRouteResult result, int errorCode) {
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null && result.getPaths().size() > 0) {
                mBusRouteResult = result;
                adapter.setDatas(result.getPaths());
            } else {
                adapter.setDatas(null);
                ToastUtil.show(this, R.string.no_result);
            }
        } else {
            adapter.setDatas(null);
            ToastUtil.show(this, "错误码：" + errorCode);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SetPointEvent event) {
        if (event != null && event.getTag().equals("RoutePlanActivity")) {
            if (event.getType().equals(Constant.POINT_TYPE_START_VALUE)) {
                tv_start.setText(event.getContent());
                startPoint = event;
            } else if (event.getType().equals(Constant.POINT_TYPE_END_VALUE)) {
                tv_end.setText(event.getContent());
                endPoint = event;
            }
            if (startPoint != null && endPoint != null) {
                searchRoute(startPoint.getLatLonPoint(), endPoint.getLatLonPoint());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
}
