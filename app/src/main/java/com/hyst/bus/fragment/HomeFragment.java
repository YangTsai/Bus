package com.hyst.bus.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.hyst.bus.R;
import com.hyst.bus.activity.RoutePlanActivity;
import com.hyst.bus.activity.SetPointActivity;
import com.hyst.bus.activity.StationDetailActivity;
import com.hyst.bus.adapter.RecyclerAdapter;
import com.hyst.bus.constant.Constant;
import com.hyst.bus.model.HistoryCache;
import com.hyst.bus.model.RecyclerHolder;
import com.hyst.bus.model.event.SetPointEvent;
import com.hyst.bus.util.HistoryCacheUtil;
import com.hyst.bus.util.ToastUtil;
import com.hyst.bus.util.ViewUtil;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;

import java.util.List;

/**
 * Created by Administrator on 2018/1/16.
 */

public class HomeFragment extends BaseFragment {

    private SpringView springView;
    private TextView tv_start;
    private TextView tv_end;
    private TextView tv_clear;
    private LinearLayout ll_history;
    private EditText et_bus;
    private TextView tv_query_bus;
    private SetPointEvent startPoint = null;
    private SetPointEvent endPoint = null;
    private ImageView iv_exchange;
    //
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private List<HistoryCache> data;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        et_bus = view.findViewById(R.id.et_bus);
        tv_query_bus = view.findViewById(R.id.tv_query_bus);
        tv_start = view.findViewById(R.id.tv_start);
        tv_end = view.findViewById(R.id.tv_end);
        iv_exchange = view.findViewById(R.id.iv_exchange);
        recyclerView = view.findViewById(R.id.recyclerView);
        ll_history = view.findViewById(R.id.ll_history);
        tv_clear = view.findViewById(R.id.tv_clear);
        springView = view.findViewById(R.id.springView);
        tv_query_bus.setOnClickListener(this);
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
        data = HistoryCacheUtil.getCache(context);
        if (data == null || data.size() == 0) {
            ll_history.setVisibility(View.GONE);
        } else {
            ll_history.setVisibility(View.VISIBLE);
        }
        adapter = new RecyclerAdapter<HistoryCache>(context, data, R.layout.item_history) {
            @Override
            public void convert(final RecyclerHolder holder, final HistoryCache cache) {
                if (cache.getType().equals("route")) {
                    holder.getView(R.id.ll_search_address).setVisibility(View.VISIBLE);
                    holder.getView(R.id.ll_search_route).setVisibility(View.GONE);
                }
                holder.setText(R.id.tv_name, cache.getStartContent() + "---" + cache.getEndContent());
                holder.setOnClickListener(R.id.tv_name, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, RoutePlanActivity.class);
                        Bundle bundle = new Bundle();
                        SetPointEvent startPoint = new SetPointEvent("HomeFragment", Constant.POINT_TYPE_START_VALUE,
                                cache.getStartContent(), new LatLonPoint(cache.getStartLat(), cache.getStartLon()));
                        SetPointEvent endPoint = new SetPointEvent("HomeFragment", Constant.POINT_TYPE_END_VALUE,
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
        data = HistoryCacheUtil.getCache(context);
        if (data == null || data.size() == 0) {
            ll_history.setVisibility(View.GONE);
        } else {
            ll_history.setVisibility(View.VISIBLE);
        }
        adapter.setDatas(data);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        super.onClick(view);
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
                intent.putExtra(Constant.POINT_TAG, "HomeFragment");
                startActivity(intent);
                break;
            case R.id.tv_end:
                intent = new Intent(context, SetPointActivity.class);
                intent.putExtra(Constant.POINT_TYPE, Constant.POINT_TYPE_END_VALUE);
                intent.putExtra(Constant.POINT_TAG, "HomeFragment");
                startActivity(intent);
                break;
            case R.id.tv_query_bus:
                if (!TextUtils.isEmpty(et_bus.getText().toString())) {
                    intent = new Intent(context, StationDetailActivity.class);
                    intent.putExtra("bus", et_bus.getText().toString());
                    startActivity(intent);
                } else {
                    ToastUtil.show(context, "请输入公交号");
                }
                break;
            case R.id.tv_clear:
                HistoryCacheUtil.clearCache(context);
                data.clear();
                adapter.setDatas(data);
                ll_history.setVisibility(View.GONE);
                break;
        }
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
}
