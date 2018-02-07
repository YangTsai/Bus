package com.hyst.bus.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.busline.BusLineItem;
import com.amap.api.services.busline.BusLineQuery;
import com.amap.api.services.busline.BusLineResult;
import com.amap.api.services.busline.BusLineSearch;
import com.amap.api.services.busline.BusStationItem;
import com.amap.api.services.core.AMapException;
import com.hyst.bus.R;
import com.hyst.bus.adapter.RecyclerAdapter;
import com.hyst.bus.model.RecyclerHolder;
import com.hyst.bus.model.cache.BusCache;
import com.hyst.bus.model.cache.LocationCache;
import com.hyst.bus.util.BusCacheUtil;
import com.hyst.bus.util.LocationUtil;
import com.hyst.bus.util.ToastUtil;
import com.hyst.bus.util.ViewUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/1/23.
 */

public class StationDetailActivity extends BaseActivity implements BusLineSearch.OnBusLineSearchListener {

    private ImageView tv_map;
    private TextView tv_bus;
    private TextView tv_name;
    private TextView tv_detail;
    private LinearLayout ll_reverse;
    private LinearLayout ll_refresh;
    private ImageView iv_back;

    //

    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private List<String> data;
    private BusLineQuery busLineQuery;
    private BusLineSearch busLineSearch;
    private String searchRoute;
    private boolean reverse = true;
    //
    private BusLineItem busLineItem;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_station_detail;
    }

    @Override
    protected void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        tv_bus = (TextView) findViewById(R.id.tv_bus);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_detail = (TextView) findViewById(R.id.tv_detail);
        ll_reverse = (LinearLayout) findViewById(R.id.ll_reverse);
        ll_refresh = (LinearLayout) findViewById(R.id.ll_refresh);
        tv_map = (ImageView) findViewById(R.id.tv_map);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_map.setOnClickListener(this);
        ll_reverse.setOnClickListener(this);
        ll_refresh.setOnClickListener(this);
        iv_back.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        String busName = getIntent().getStringExtra("bus");
        //判断是否是数字
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(busName);
        if (isNum.matches()) {
            searchRoute = busName + "路";
        } else {
            searchRoute = busName;
        }
        tv_bus.setText(searchRoute);
        data = new ArrayList<>();
        adapter = new RecyclerAdapter<String>(this, data, R.layout.item_station) {
            @Override
            public void convert(RecyclerHolder holder, String s) {
                holder.setText(R.id.tv_count, holder.getAdapterPosition() + 1 + "");
                holder.setText(R.id.tv_station_name, s);
                if (holder.getAdapterPosition() == data.size() / 2) {
                    ((TextView) holder.getView(R.id.tv_station_name)).setTextColor(getResources().getColor(R.color.station_red2));
                    ((TextView) holder.getView(R.id.tv_count)).setTextColor(getResources().getColor(R.color.station_red2));
                    holder.setImageResource(R.id.iv_jiantou, R.drawable.route_jiantou_red);
                    holder.getView(R.id.iv_bus).setVisibility(View.VISIBLE);
                } else {
                    ((TextView) holder.getView(R.id.tv_station_name)).setTextColor(getResources().getColor(R.color.colorFont));
                    ((TextView) holder.getView(R.id.tv_count)).setTextColor(getResources().getColor(R.color.colorFont));
                    holder.setImageResource(R.id.iv_jiantou, R.drawable.route_jiantou);
                    holder.getView(R.id.iv_bus).setVisibility(View.INVISIBLE);
                    if (holder.getAdapterPosition() == 10 || holder.getAdapterPosition() == 17) {
                        holder.getView(R.id.iv_bus).setVisibility(View.VISIBLE);
                        holder.setImageResource(R.id.iv_bus, R.drawable.ic_bus_route);
                        holder.getView(R.id.iv_bus).setPadding(0, 0, 0, 0);
                    }
                }
            }
        };
        recyclerView = ViewUtil.getHRowsNoLine(this, recyclerView, 1);
        recyclerView.setAdapter(adapter);
        searchBusLine();
    }

    private void searchBusLine() {
        LocationCache locationCache = LocationUtil.getIns(this).getCurrentLocation();
        busLineQuery = new BusLineQuery(searchRoute, BusLineQuery.SearchType.BY_LINE_NAME, locationCache.getCityName());
        busLineQuery.setPageSize(10);
        busLineQuery.setPageNumber(1);
        busLineSearch = new BusLineSearch(this, busLineQuery);
        busLineSearch.setOnBusLineSearchListener(this);
        busLineSearch.searchBusLineAsyn();
    }

    @Override
    public void onBusLineSearched(BusLineResult result, int rCode) {
        data.clear();
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null
                    && result.getQuery().equals(busLineQuery)) {
                if (result.getQuery().getCategory() == BusLineQuery.SearchType.BY_LINE_NAME) {
                    if (result.getPageCount() > 0 && result.getBusLines() != null
                            && result.getBusLines().size() > 0) {
                        List<BusLineItem> lines = result.getBusLines();
                        if (lines != null && lines.size() > 0) {
                            if (reverse && lines.size() > 1) {
                                busLineItem = lines.get(0);
                            } else {
                                busLineItem = lines.get(1);
                            }
                        } else {
                            return;
                        }
                        List<BusStationItem> busStations = busLineItem.getBusStations();
                        for (int i = 0; i < busStations.size(); i++) {
                            data.add(busStations.get(i).getBusStationName());
                        }
                        adapter.setDatas(data);
                        recyclerView.scrollToPosition(data.size() / 2 - 3);
                        runLayoutAnimation(recyclerView);
                        //
                        DateFormat df = new SimpleDateFormat("HH:mm");
                        String startTime = "";
                        String endTime = "";
                        double price = 0;
                        if (busLineItem.getFirstBusTime() != null && busLineItem.getLastBusTime() != null) {
                            startTime = df.format(busLineItem.getFirstBusTime());
                            endTime = df.format(busLineItem.getLastBusTime());
                        }
                        if (busLineItem.getBasicPrice() != 0) {
                            price = busLineItem.getBasicPrice();
                        }
                        tv_detail.setText("首班 " + startTime + "  末班 " + endTime + "  票价 " + price + "元");
                        if (!TextUtils.isEmpty(busLineItem.getOriginatingStation()) && !TextUtils.isEmpty(busLineItem.getTerminalStation())) {
                            tv_name.setText(busLineItem.getOriginatingStation() + "---" + busLineItem.getTerminalStation());
                        }
                        BusCache busCache = new BusCache();
                        busCache.setBusName(searchRoute);
                        busCache.setOriginationStation(busLineItem.getOriginatingStation());
                        busCache.setTerminusStation(busLineItem.getTerminalStation());
                        busCache.setStartTime(startTime);
                        busCache.setEndTime(endTime);
                        busCache.setPrice(price);
                        BusCacheUtil.setCache(this, busCache);
                    }
                }
            } else {
                ToastUtil.show(StationDetailActivity.this, R.string.no_result);
            }
        } else {
            ToastUtil.show(StationDetailActivity.this, rCode);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_reverse:
                reverse = !reverse;
                busLineSearch.searchBusLineAsyn();
                break;
            case R.id.ll_refresh:
                busLineSearch.searchBusLineAsyn();
                break;
            case R.id.tv_map:
                Intent intent = new Intent(this, BusMapActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("bus", busLineItem);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void runLayoutAnimation(RecyclerView recyclerView) {
        Context context = recyclerView.getContext();
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);
        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
}
