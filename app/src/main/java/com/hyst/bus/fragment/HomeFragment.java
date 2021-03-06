package com.hyst.bus.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.busline.BusLineItem;
import com.amap.api.services.busline.BusLineQuery;
import com.amap.api.services.busline.BusLineResult;
import com.amap.api.services.busline.BusLineSearch;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.hyst.bus.R;
import com.hyst.bus.activity.CityListActivity;
import com.hyst.bus.activity.StationDetailActivity;
import com.hyst.bus.adapter.RecyclerAdapter;
import com.hyst.bus.constant.Constant;
import com.hyst.bus.dialog.DeleteDialog;
import com.hyst.bus.model.BusStationInfo;
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

public class HomeFragment extends BaseFragment implements PoiSearch.OnPoiSearchListener, BusLineSearch.OnBusLineSearchListener {

    private SpringView springView;
    private EditText et_bus;
    private TextView tv_city;
    private TextView tv_city_name;
    private TextView tv_query_bus;
    //
    private RecyclerView re_bus;
    private RecyclerAdapter adapter_bus;
    private List<BusStationInfo> data_bus;
    //
    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private List<BusCache> data;
    private TextView tv_clear;

    private PoiSearch.Query query;
    private PoiSearch poiSearch;

    private DeleteDialog deleteDialog;

    private LatLng latLng;
    //
    private BusLineQuery busLineQuery;
    private BusLineSearch busLineSearch;

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
                holder.setText(R.id.tv_name, cache.getBusName());
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
        adapter_bus = new RecyclerAdapter<BusStationInfo>(context, data_bus, R.layout.item_home_bus) {
            @Override
            public void convert(final RecyclerHolder holder, final BusStationInfo info) {
                if (holder.getAdapterPosition() == 0) {
                    holder.getView(R.id.view_line).setVisibility(View.VISIBLE);
                    holder.getView(R.id.rl_address).setVisibility(View.VISIBLE);
                } else {
                    holder.getView(R.id.view_line).setVisibility(View.GONE);
                    holder.getView(R.id.rl_address).setVisibility(View.GONE);
                }
                float distance = AMapUtils.calculateLineDistance(info.getLatLng(), latLng);
                if (distance > 1000) {
                    holder.setText(R.id.tv_distance, "大于1000米");
                } else {
                    holder.setText(R.id.tv_distance, (int) distance + "米");
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
            latLng = new LatLng(locationCache.getLatitude(), locationCache.getLongitude());
            tv_city_name.setText(locationCache.getCityName());
            query = new PoiSearch.Query(Constant.POI_BUS, "", locationCache.getCityName());
            query.setPageSize(1);// 设置每页最多返回多少条poiitem
            query.setPageNum(1);//设置查询页码
            poiSearch = new PoiSearch(context, query);
            poiSearch.setOnPoiSearchListener(this);
            //设置周边搜索的中心点以及半径
            poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(locationCache.getLatitude(), locationCache.getLongitude()), 1000));
        } else {
            latLng = new LatLng(event.getLatLonPoint().getLatitude(), event.getLatLonPoint().getLongitude());
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
                    searchBusLine();
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
                intent = new Intent(context, CityListActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void searchBusLine() {
        LocationCache locationCache = LocationUtil.getIns(context).getCurrentLocation();
        busLineQuery = new BusLineQuery(et_bus.getText().toString(), BusLineQuery.SearchType.BY_LINE_NAME, locationCache.getCityName());
        busLineQuery.setPageSize(10);
        busLineQuery.setPageNumber(1);
        busLineSearch = new BusLineSearch(context, busLineQuery);
        busLineSearch.setOnBusLineSearchListener(this);
        busLineSearch.searchBusLineAsyn();
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
                LatLonPoint latLonPoint = pois.get(0).getLatLonPoint();
                String snippet = pois.get(0).getSnippet();
                String title = pois.get(0).getTitle();
                if (!TextUtils.isEmpty(snippet)) {
                    if (snippet.contains(";")) {
                        String[] split = snippet.split(";");
                        for (int j = 0; j < split.length; j++) {
                            BusStationInfo busStationInfo = new BusStationInfo();
                            busStationInfo.setStationName(title);
                            busStationInfo.setLatLng(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude()));
                            busStationInfo.setBusName(split[j]);
                            data_bus.add(busStationInfo);
                            if (j == 1) {
                                break;
                            }
                        }
                    } else {
                        BusStationInfo busStationInfo = new BusStationInfo();
                        busStationInfo.setStationName(title);
                        busStationInfo.setBusName(snippet);
                        busStationInfo.setLatLng(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude()));
                        data_bus.add(busStationInfo);
                    }
                    adapter_bus.setDatas(data_bus);
                }
            }
        }

    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onBusLineSearched(BusLineResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null) {
                if (result.getQuery().getCategory() == BusLineQuery.SearchType.BY_LINE_NAME) {
                    if (result.getPageCount() > 0 && result.getBusLines() != null && result.getBusLines().size() > 0) {
                        List<BusLineItem> lines = result.getBusLines();
                        if (lines == null && lines.size() == 0) {
                            ToastUtil.show(context, R.string.no_result);
                        }
                        Intent intent = new Intent(context, StationDetailActivity.class);
                        intent.putExtra("bus", et_bus.getText().toString());
                        startActivity(intent);
                    } else {
                        ToastUtil.show(context, R.string.no_result);
                    }
                } else {
                    ToastUtil.show(context, R.string.no_result);
                }
            } else {
                ToastUtil.show(context, R.string.no_result);
            }
        } else {
            ToastUtil.show(context, rCode);
        }
    }
}
