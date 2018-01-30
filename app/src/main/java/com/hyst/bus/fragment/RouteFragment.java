package com.hyst.bus.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.hyst.bus.R;
import com.hyst.bus.activity.RoutePlanActivity;
import com.hyst.bus.activity.SetPointActivity;
import com.hyst.bus.adapter.RecyclerAdapter;
import com.hyst.bus.constant.Constant;
import com.hyst.bus.model.cache.RouteCache;
import com.hyst.bus.model.RecyclerHolder;
import com.hyst.bus.model.event.SetPointEvent;
import com.hyst.bus.util.RouteCacheUtil;
import com.hyst.bus.util.ViewUtil;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;

import java.util.List;

/**
 * Created by Administrator on 2018/1/16.
 */

public class RouteFragment extends BaseFragment {
    private TextView tv_start;
    private TextView tv_end;
    private TextView tv_clear;
    private SetPointEvent startPoint = null;
    private SetPointEvent endPoint = null;
    private ImageView iv_exchange;
    //历史记录
    private SpringView springView;
    private LinearLayout ll_history;
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private List<RouteCache> data;


    @Override
    protected int setLayoutId() {
        return R.layout.fragment_route;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        tv_start = (TextView) view.findViewById(R.id.tv_start);
        tv_end = (TextView) view.findViewById(R.id.tv_end);
        tv_clear = (TextView) view.findViewById(R.id.tv_clear);
        iv_exchange = (ImageView) view.findViewById(R.id.iv_exchange);
        ll_history = (LinearLayout) view.findViewById(R.id.ll_history);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        springView = (SpringView) view.findViewById(R.id.springView);
        iv_exchange.setOnClickListener(this);
        tv_start.setOnClickListener(this);
        tv_end.setOnClickListener(this);
        tv_clear.setOnClickListener(this);
        springView.setType(SpringView.Type.FOLLOW);
        springView.setHeader(new AliHeader(context));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                updateData();
                springView.onFinishFreshAndLoad();
            }

            @Override
            public void onLoadmore() {

            }
        });
    }

    @Override
    protected void initData() {
        data = RouteCacheUtil.getCache(context);
        if (data == null || data.size() == 0) {
            ll_history.setVisibility(View.GONE);
        } else {
            ll_history.setVisibility(View.VISIBLE);
        }
        adapter = new RecyclerAdapter<RouteCache>(context, data, R.layout.item_history) {
            @Override
            public void convert(final RecyclerHolder holder, final RouteCache cache) {
                holder.setText(R.id.tv_name, cache.getStartContent() + "---" + cache.getEndContent());
                holder.setOnClickListener(R.id.tv_name, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, RoutePlanActivity.class);
                        Bundle bundle = new Bundle();
                        SetPointEvent startPoint = new SetPointEvent(getClass().getName(), Constant.POINT_TYPE_START_VALUE,
                                cache.getStartContent(), new LatLonPoint(cache.getStartLat(), cache.getStartLon()));
                        SetPointEvent endPoint = new SetPointEvent(getClass().getName(), Constant.POINT_TYPE_END_VALUE,
                                cache.getEndContent(), new LatLonPoint(cache.getEndLat(), cache.getEndLon()));
                        bundle.putParcelable("startPoint", startPoint);
                        bundle.putParcelable("endPoint", endPoint);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerView = ViewUtil.getVRowsNoLine(context, recyclerView, 1);
        recyclerView.setAdapter(adapter);
    }

    private void updateData() {
        data = RouteCacheUtil.getCache(context);
        if (data == null || data.size() == 0) {
            ll_history.setVisibility(View.GONE);
        } else {
            ll_history.setVisibility(View.VISIBLE);
        }
        adapter.setDatas(data);
    }


    public void setLocation(SetPointEvent event) {
        if (event.getType().equals(Constant.POINT_TYPE_START_VALUE)) {
            tv_start.setText(event.getContent());
            startPoint = event;
        } else if (event.getType().equals(Constant.POINT_TYPE_END_VALUE)) {
            tv_end.setText(event.getContent());
            endPoint = event;
        }
        if (!TextUtils.isEmpty(tv_start.getText().toString()) && !TextUtils.isEmpty(tv_end.getText().toString())
                && startPoint != null && endPoint != null) {
            Intent intent = new Intent(context, RoutePlanActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("startPoint", startPoint);
            bundle.putParcelable("endPoint", endPoint);
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_exchange:
                if (startPoint != null && endPoint != null) {
                    SetPointEvent event = startPoint;
                    startPoint = endPoint;
                    endPoint = event;
                    tv_start.setText(startPoint.getContent());
                    tv_end.setText(endPoint.getContent());
                    intent = new Intent(context, RoutePlanActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("startPoint", startPoint);
                    bundle.putParcelable("endPoint", endPoint);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            case R.id.tv_start:
                intent = new Intent(context, SetPointActivity.class);
                intent.putExtra(Constant.POINT_TYPE, Constant.POINT_TYPE_START_VALUE);
                intent.putExtra(Constant.POINT_TAG, getClass().getName());
                startActivity(intent);
                break;
            case R.id.tv_end:
                intent = new Intent(context, SetPointActivity.class);
                intent.putExtra(Constant.POINT_TYPE, Constant.POINT_TYPE_END_VALUE);
                intent.putExtra(Constant.POINT_TAG, getClass().getName());
                startActivity(intent);
                break;
            case R.id.tv_clear:
                RouteCacheUtil.clearCache(context);
                data.clear();
                adapter.setDatas(data);
                ll_history.setVisibility(View.GONE);
                break;
        }
    }
}
