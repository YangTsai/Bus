package com.hyst.bus.activity;

import android.content.Context;
import android.content.Intent;
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
import com.hyst.bus.util.ToastUtil;
import com.hyst.bus.util.ViewUtil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/23.
 */

public class StationDetailActivity extends BaseActivity implements BusLineSearch.OnBusLineSearchListener {

    private TextView tv_map;
    private TextView tv_bus;
    private TextView tv_name;
    private TextView tv_detail;
    private TextView tv_reverse;
    private TextView tv_refresh;
    private ImageView iv_back;

    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private List<String> data;
    private BusLineQuery busLineQuery;
    private BusLineSearch busLineSearch;
    private String searchRoute;
    private boolean reverse = true;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_station_detail;
    }

    @Override
    protected void initView() {
        recyclerView = findViewById(R.id.recyclerView);
        tv_bus = findViewById(R.id.tv_bus);
        tv_name = findViewById(R.id.tv_name);
        tv_detail = findViewById(R.id.tv_detail);
        tv_reverse = findViewById(R.id.tv_reverse);
        tv_refresh = findViewById(R.id.tv_refresh);
        tv_map = findViewById(R.id.tv_map);
        iv_back = findViewById(R.id.iv_back);
        tv_map.setOnClickListener(this);
        tv_reverse.setOnClickListener(this);
        tv_refresh.setOnClickListener(this);
        iv_back.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        searchRoute = getIntent().getStringExtra("bus");
        tv_bus.setText(searchRoute + "路");
        data = new ArrayList<>();
        adapter = new RecyclerAdapter<String>(this, data, R.layout.item_station) {
            @Override
            public void convert(RecyclerHolder holder, String s) {
                holder.setText(R.id.tv_count, holder.getAdapterPosition() + 1 + "");
                holder.setText(R.id.tv_station_name, s);
                ImageView view = holder.getView(R.id.iv_bus);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(80, 80);
                view.setLayoutParams(params);
                view.setImageResource(R.drawable.ic_bus_route);
                params.setMargins(50, 0, 0, 0);
                if (holder.getAdapterPosition() == data.size() / 2) {
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.INVISIBLE);
                }
            }
        };
        recyclerView = ViewUtil.getHRowsNoLine(this, recyclerView, 1);
        recyclerView.setAdapter(adapter);
        searchBusLine();
    }

    private void searchBusLine() {
        busLineQuery = new BusLineQuery(searchRoute, BusLineQuery.SearchType.BY_LINE_NAME, "西安市");
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
                        BusLineItem busLineItem = null;
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
                        String price = "";
                        if (busLineItem.getFirstBusTime() != null && busLineItem.getLastBusTime() != null) {
                            startTime = df.format(busLineItem.getFirstBusTime());
                            endTime = df.format(busLineItem.getLastBusTime());
                        }
                        if (busLineItem.getBasicPrice() != 0) {
                            price = busLineItem.getBasicPrice() + "";
                        }
                        tv_detail.setText("首班 " + startTime + "  末班 " + endTime + "  票价 " + price);
                        if (!TextUtils.isEmpty(busLineItem.getOriginatingStation()) && !TextUtils.isEmpty(busLineItem.getTerminalStation())) {
                            tv_name.setText(busLineItem.getOriginatingStation() + "---" + busLineItem.getTerminalStation());
                        }
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
            case R.id.tv_reverse:
                reverse = !reverse;
                busLineSearch.searchBusLineAsyn();
                break;
            case R.id.tv_refresh:
                busLineSearch.searchBusLineAsyn();
                break;
            case R.id.tv_map:
                Intent intent = new Intent(this, BusMapActivity.class);
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
