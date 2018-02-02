package com.hyst.bus.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.busline.BusLineItem;
import com.amap.api.services.busline.BusLineQuery;
import com.amap.api.services.busline.BusLineResult;
import com.amap.api.services.busline.BusLineSearch;
import com.amap.api.services.busline.BusStationItem;
import com.amap.api.services.core.AMapException;
import com.hyst.bus.R;
import com.hyst.bus.model.cache.LocationCache;
import com.hyst.bus.util.LocationUtil;
import com.hyst.bus.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/16.
 */

public class MapFragment extends BaseFragment implements BusLineSearch.OnBusLineSearchListener,
        AMap.OnMapClickListener, AMap.InfoWindowAdapter {
    private MapView mapView;
    private AMap aMap;
    private EditText et_search;
    private TextView tv_search;
    //
    private LocationCache locationCache;
    private BusLineQuery busLineQuery;

    private Marker mMarker;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_map;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        tv_search = (TextView) view.findViewById(R.id.tv_search);
        et_search = (EditText) view.findViewById(R.id.et_search);
        mapView = (MapView) view.findViewById(R.id.map);
        tv_search.setOnClickListener(this);
        mapView.onCreate(savedInstanceState);
    }

    @Override
    protected void initData() {
        aMap = mapView.getMap();
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setInfoWindowAdapter(this);
        aMap.setOnMapClickListener(this);
        locationCache = LocationUtil.getIns(context).getCurrentLocation();
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(locationCache.getLatitude(), locationCache.getLongitude())));
    }


    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();

    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mapView.onDestroy();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_search:
                if (!TextUtils.isEmpty(et_search.getText().toString())) {
                    busLineQuery = new BusLineQuery(et_search.getText().toString(), BusLineQuery.SearchType.BY_LINE_NAME, locationCache.getCityName());
                    busLineQuery.setPageSize(10);
                    busLineQuery.setPageNumber(1);
                    BusLineSearch busLineSearch = new BusLineSearch(context, busLineQuery);
                    busLineSearch.setOnBusLineSearchListener(this);
                    busLineSearch.searchBusLineAsyn();
                } else {
                    ToastUtil.show(context, "请输入公交号");
                }
                break;
        }
    }

    @Override
    public void onBusLineSearched(BusLineResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null
                    && result.getQuery().equals(busLineQuery)) {
                if (result.getQuery().getCategory() == BusLineQuery.SearchType.BY_LINE_NAME) {
                    if (result.getPageCount() > 0 && result.getBusLines() != null
                            && result.getBusLines().size() > 0) {
                        List<BusLineItem> lines = result.getBusLines();
                        BusLineItem busLineItem = lines.get(0);
                        List<BusStationItem> busStations = busLineItem.getBusStations();
                        List<LatLng> latLngs = new ArrayList<>();
                        for (BusStationItem item : busStations) {
                            LatLng latLng = new LatLng(item.getLatLonPoint().getLatitude(), item.getLatLonPoint().getLongitude());
                            latLngs.add(latLng);
                            aMap.moveCamera(CameraUpdateFactory.zoomTo(12));

                            aMap.addMarker(new MarkerOptions().position(latLng).title(item.getBusStationName()).icon(
                                    BitmapDescriptorFactory.fromResource(R.drawable.map_route_yuan)));
                        }
                        aMap.addPolyline(new PolylineOptions().
                                addAll(latLngs).width(20).color(Color.parseColor("#00EE00")));
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

    @Override
    public void onMapClick(LatLng latLng) {
        if (mMarker != null) {
            mMarker.hideInfoWindow();
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {

        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        mMarker = marker;
        View infoContent = LayoutInflater.from(context).inflate(R.layout.layout_mark_info, null);
        TextView tv_mark = (TextView) infoContent.findViewById(R.id.tv_mark);
        tv_mark.setText(marker.getTitle());
        return infoContent;
    }
}
