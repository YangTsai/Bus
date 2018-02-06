package com.hyst.bus.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.hyst.bus.R;
import com.hyst.bus.activity.StationDetailActivity;
import com.hyst.bus.adapter.RecyclerAdapter;
import com.hyst.bus.constant.Constant;
import com.hyst.bus.dialog.DeleteDialog;
import com.hyst.bus.model.BusInfo;
import com.hyst.bus.model.RecyclerHolder;
import com.hyst.bus.model.cache.BusCache;
import com.hyst.bus.model.cache.LocationCache;
import com.hyst.bus.model.event.SetPointEvent;
import com.hyst.bus.util.BusCacheUtil;
import com.hyst.bus.util.LocationUtil;
import com.hyst.bus.util.ToastUtil;
import com.hyst.bus.util.ViewUtil;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/16.
 */

public class HomeFragment extends BaseFragment implements PoiSearch.OnPoiSearchListener {

    private SpringView springView;
    private EditText et_bus;
    private TextView tv_city;
    private TextView tv_city_name;
    private TextView tv_query_bus;
    //
    private RecyclerView re_bus;
    private RecyclerAdapter adapter_bus;
    private List<BusInfo> data_bus;
    //
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private List<BusCache> data;
    private TextView tv_clear;

    private PoiSearch.Query query;
    private PoiSearch poiSearch;

    private DeleteDialog deleteDialog;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        tv_city = (TextView) view.findViewById(R.id.tv_city);
        tv_city_name = (TextView) view.findViewById(R.id.tv_city_name);
        et_bus = (EditText) view.findViewById(R.id.et_bus);
        tv_query_bus = (TextView) view.findViewById(R.id.tv_query_bus);
        re_bus = (RecyclerView) view.findViewById(R.id.re_bus);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        tv_clear = (TextView) view.findViewById(R.id.tv_clear);
        springView = (SpringView) view.findViewById(R.id.springView);
        tv_query_bus.setOnClickListener(this);
        tv_city.setOnClickListener(this);

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
        deleteDialog = new DeleteDialog(context);
        data_bus = new ArrayList<>();
        data = BusCacheUtil.getCache(context);
        if (data == null || data.size() == 0) {
            tv_clear.setText("暂无历史记录");
        } else {
            tv_clear.setText("清除历史记录");
        }
        adapter = new RecyclerAdapter<BusCache>(context, data, R.layout.item_bus_history) {
            @Override
            public void convert(final RecyclerHolder holder, final BusCache cache) {
                holder.setText(R.id.tv_name, cache.getBusName() + "路");
                holder.setText(R.id.tv_address, cache.getOriginationStation() + "--" + cache.getTerminusStation());
                holder.setOnClickListener(R.id.ll_search_address, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, StationDetailActivity.class);
                        intent.putExtra("bus", cache.getBusName());
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerView = ViewUtil.getVRowsNoLine(context, recyclerView, 1);
        recyclerView.setAdapter(adapter);
        //
        adapter_bus = new RecyclerAdapter<BusInfo>(context, data_bus, R.layout.item_home_bus) {
            @Override
            public void convert(final RecyclerHolder holder, final BusInfo info) {
                if (holder.getAdapterPosition() == 0) {
                    holder.getView(R.id.view_line).setVisibility(View.VISIBLE);
                    holder.getView(R.id.rl_address).setVisibility(View.VISIBLE);
                } else {
                    holder.getView(R.id.view_line).setVisibility(View.GONE);
                    holder.getView(R.id.rl_address).setVisibility(View.GONE);
                }
                holder.setText(R.id.tv_station_name, info.getStationName());
                holder.setText(R.id.tv_bus_name, info.getBusName());
                holder.setOnClickListener(R.id.ll_bus_info, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, StationDetailActivity.class);
                        intent.putExtra("bus", info.getBusName());
                        startActivity(intent);
                    }
                });
            }
        };
        re_bus = ViewUtil.getVRowsNoLine(context, re_bus, 1);
        re_bus.setAdapter(adapter_bus);
    }

    public void setLocation(SetPointEvent event) {
        if (event.getLatLonPoint() == null) {
            LocationCache locationCache = LocationUtil.getIns(context).getCurrentLocation();
            tv_city_name.setText(locationCache.getCityName());
            query = new PoiSearch.Query(Constant.POI_BUS, "", locationCache.getCityName());
            query.setPageSize(1);// 设置每页最多返回多少条poiitem
            query.setPageNum(1);//设置查询页码
            poiSearch = new PoiSearch(context, query);
            poiSearch.setOnPoiSearchListener(this);
            //设置周边搜索的中心点以及半径
            poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(locationCache.getLatitude(), locationCache.getLongitude()), 1000));
        } else {
            tv_city_name.setText(event.getaMapLocation().getCity());
            query = new PoiSearch.Query(Constant.POI_BUS, "", event.getaMapLocation().getCity());
            query.setPageSize(1);// 设置每页最多返回多少条poiitem
            query.setPageNum(1);//设置查询页码
            poiSearch = new PoiSearch(context, query);
            poiSearch.setOnPoiSearchListener(this);
            //设置周边搜索的中心点以及半径
            poiSearch.setBound(new PoiSearch.SearchBound(event.getLatLonPoint(), 1000));
        }
        poiSearch.searchPOIAsyn();
    }

    private void updateData() {
        poiSearch.searchPOIAsyn();
        data = BusCacheUtil.getCache(context);
        if (data == null || data.size() == 0) {
            tv_clear.setText("暂无历史记录");
        } else {
            tv_clear.setText("清除历史记录");
        }
        adapter.setDatas(data);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        super.onClick(view);
        switch (view.getId()) {
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
                if (data != null && data.size() > 0) {
                    deleteDialog.showDialog("BusCache", this);
                }
                break;
            case R.id.tv_city:

                break;
        }
    }


    public void delete() {
        data.clear();
        adapter.setDatas(data);
        tv_clear.setText("暂无历史记录");
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        if (i == 1000 && poiResult != null) {
            data_bus.clear();
            ArrayList<PoiItem> pois = poiResult.getPois();
            if (pois != null && pois.size() > 0) {
                String snippet = pois.get(0).getSnippet();
                String title = pois.get(0).getTitle();
                if (!TextUtils.isEmpty(snippet)) {
                    if (snippet.contains(";")) {
                        String[] split = snippet.split(";");
                        for (int j = 0; j < split.length; j++) {
                            BusInfo busInfo = new BusInfo();
                            busInfo.setStationName(title);
                            busInfo.setBusName(split[j]);
                            data_bus.add(busInfo);
                            if (j == 1) {
                                break;
                            }
                        }
                    } else {
                        BusInfo busInfo = new BusInfo();
                        busInfo.setStationName(title);
                        busInfo.setBusName(snippet);
                        data_bus.add(busInfo);
                    }
                    adapter_bus.setDatas(data_bus);
                }
            }
        }

    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

}
