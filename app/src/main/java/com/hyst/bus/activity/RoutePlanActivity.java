package com.hyst.bus.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.hyst.bus.R;
import com.hyst.bus.adapter.RecyclerAdapter;
import com.hyst.bus.constant.Constant;
import com.hyst.bus.custom.AppPopupWindow;
import com.hyst.bus.model.RecyclerHolder;
import com.hyst.bus.model.cache.LocationCache;
import com.hyst.bus.model.event.SetPointEvent;
import com.hyst.bus.util.AMapUtil;
import com.hyst.bus.util.LocationUtil;
import com.hyst.bus.util.RouteCacheUtil;
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
    private TextView tv_go_time;
    private TextView tv_route_choose;
    private ImageView iv_go_time;
    private ImageView iv_route_choose;
    //
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private List<BusPath> data;
    private BusRouteResult mBusRouteResult;
    //
    private SetPointEvent startPoint;
    private SetPointEvent endPoint;
    //
    private RouteSearch routeSearch;
    private RouteSearch.BusRouteQuery query;
    //
    private AppPopupWindow popupWindow;


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
        tv_start = (TextView) findViewById(R.id.tv_start);
        tv_end = (TextView) findViewById(R.id.tv_end);
        tv_start.setOnClickListener(this);
        tv_end.setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        iv_exchange = (ImageView) findViewById(R.id.iv_exchange);
        iv_exchange.setOnClickListener(this);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_go_time = (TextView) findViewById(R.id.tv_go_time);
        tv_route_choose = (TextView) findViewById(R.id.tv_route_choose);
        iv_go_time = (ImageView) findViewById(R.id.iv_go_time);
        iv_route_choose = (ImageView) findViewById(R.id.iv_route_choose);
        tv_go_time.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_route_choose.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        startPoint = getIntent().getParcelableExtra("startPoint");
        endPoint = getIntent().getParcelableExtra("endPoint");
        if (startPoint == null && endPoint == null) {
            return;
        }
        RouteCacheUtil.setCache(this, startPoint, endPoint);
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
        //
        routeSearch = new RouteSearch(this);
        routeSearch.setRouteSearchListener(this);
        searchRoute(RouteSearch.BUS_DEFAULT);
    }

    /**
     * 按方式搜索方案
     *
     * @param mode
     */
    private void searchRoute(int mode) {
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint.getLatLonPoint(), endPoint.getLatLonPoint());
        LocationCache locationCache = LocationUtil.getIns(this).getCurrentLocation();
        query = new RouteSearch.BusRouteQuery(fromAndTo, mode, locationCache.getCityName(), 0);
        //开始规划路径
        routeSearch.calculateBusRouteAsyn(query);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent;
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
                    searchRoute(RouteSearch.BUS_DEFAULT);
                }
                break;
            case R.id.tv_start:
                intent = new Intent(this, SetPointActivity.class);
                intent.putExtra(Constant.POINT_TYPE, Constant.POINT_TYPE_START_VALUE);
                intent.putExtra(Constant.POINT_TAG, getClass().getName());
                startActivity(intent);
                break;
            case R.id.tv_end:
                intent = new Intent(this, SetPointActivity.class);
                intent.putExtra(Constant.POINT_TYPE, Constant.POINT_TYPE_END_VALUE);
                intent.putExtra(Constant.POINT_TAG, getClass().getName());
                startActivity(intent);
                break;
            case R.id.tv_route_choose:
                tv_route_choose.setTextColor(getResources().getColor(R.color.colorFont));
                tv_go_time.setTextColor(getResources().getColor(R.color.colorGray));
                iv_route_choose.setImageResource(R.drawable.layer_down);
                setRoutePw();
                break;
            case R.id.tv_go_time:
                tv_go_time.setTextColor(getResources().getColor(R.color.colorFont));
                tv_route_choose.setTextColor(getResources().getColor(R.color.colorGray));
                break;
        }
    }

    private void setRoutePw() {
        if (popupWindow == null) {
            View contentView = LayoutInflater.from(this).inflate(R.layout.pw_route_choose, null);
            final TextView tv_walk = (TextView) contentView.findViewById(R.id.tv_walk);
            final TextView tv_money = (TextView) contentView.findViewById(R.id.tv_money);
            final TextView tv_comfort = (TextView) contentView.findViewById(R.id.tv_comfort);
            final TextView tv_distance = (TextView) contentView.findViewById(R.id.tv_distance);
            final TextView tv_exchange = (TextView) contentView.findViewById(R.id.tv_exchange);
            final TextView tv_no_subway = (TextView) contentView.findViewById(R.id.tv_no_subway);
            popupWindow = new AppPopupWindow(contentView,
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            popupWindow.setTouchable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(dw);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    iv_route_choose.setImageResource(R.drawable.layer_top);
                    adapter.setDatas(data);
                }
            });
            //最经济
            tv_money.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchRoute(RouteSearch.BUS_SAVE_MONEY);
                    tv_route_choose.setText(tv_money.getText().toString());
                    popupWindow.dismiss();
                }
            });
            //最舒适
            tv_comfort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchRoute(RouteSearch.BUS_COMFORTABLE);
                    tv_route_choose.setText(tv_comfort.getText().toString());
                    popupWindow.dismiss();
                }
            });
            //步行最少
            tv_walk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchRoute(RouteSearch.BUS_LEASE_WALK);
                    tv_route_choose.setText(tv_walk.getText().toString());
                    popupWindow.dismiss();
                }
            });
            //距离最短（最快捷）
            tv_distance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchRoute(RouteSearch.BUS_DEFAULT);
                    tv_route_choose.setText(tv_distance.getText().toString());
                    popupWindow.dismiss();
                }
            });
            //最少换乘
            tv_exchange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchRoute(RouteSearch.BUS_LEASE_CHANGE);
                    tv_route_choose.setText(tv_exchange.getText().toString());
                    popupWindow.dismiss();
                }
            });
            //不做地铁
            tv_no_subway.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchRoute(RouteSearch.BUS_NO_SUBWAY);
                    tv_route_choose.setText(tv_no_subway.getText().toString());
                    popupWindow.dismiss();
                }
            });
        }
        popupWindow.showAsDropDown(tv_route_choose);
    }

    @Override
    public void onBusRouteSearched(BusRouteResult result, int errorCode) {
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null && result.getPaths().size() > 0) {
                mBusRouteResult = result;
                data = result.getPaths();
                adapter.setDatas(result.getPaths());
            } else {
                adapter.setDatas(data);
                ToastUtil.show(this, R.string.no_result);
            }
        } else {
            data = null;
            adapter.setDatas(data);
            ToastUtil.show(this, "错误码：" + errorCode);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SetPointEvent event) {
        if (event != null && event.getTag().equals(getClass().getName())) {
            if (event.getType().equals(Constant.POINT_TYPE_START_VALUE)) {
                tv_start.setText(event.getContent());
                startPoint = event;
            } else if (event.getType().equals(Constant.POINT_TYPE_END_VALUE)) {
                tv_end.setText(event.getContent());
                endPoint = event;
            }
            if (startPoint != null && endPoint != null) {
                searchRoute(RouteSearch.BUS_DEFAULT);
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
