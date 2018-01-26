package com.hyst.bus.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import com.hyst.bus.constant.Constant;
import com.hyst.bus.model.cache.LocationCache;
import com.hyst.bus.util.ACache;
import com.hyst.bus.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/24.
 */

public class BusMapActivity extends BaseActivity implements BusLineSearch.OnBusLineSearchListener,AMap.OnMapLoadedListener,
        AMap.OnMapClickListener, AMap.InfoWindowAdapter{

    private MapView mapView;
    private AMap aMap;
    private ImageView iv_back;
    private Bundle savedInstanceState;
    //
    private BusLineQuery busLineQuery;

    private Marker mMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_bus_map;
    }

    @Override
    protected void initView() {
        iv_back =  findViewById(R.id.iv_back);
        mapView =  findViewById(R.id.map_bus);
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();
        aMap.setOnMapClickListener(this);
        aMap.setOnMapLoadedListener(this);
        aMap.setInfoWindowAdapter(this);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        LocationCache locationCache = (LocationCache) ACache.get(this).getAsObject(Constant.LOCATION_CONFIG);
        if (locationCache != null) {
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(locationCache.getLatitude(), locationCache.getLongitude())));
        }
        String bus = getIntent().getStringExtra("bus");
        busLineQuery = new BusLineQuery(bus, BusLineQuery.SearchType.BY_LINE_NAME, locationCache.getCityName());
        busLineQuery.setPageSize(1);
        busLineQuery.setPageNumber(1);
        BusLineSearch busLineSearch = new BusLineSearch(this, busLineQuery);
        busLineSearch.setOnBusLineSearchListener(this);
        busLineSearch.searchBusLineAsyn();
    }

    @Override
    public void onMapLoaded() {

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
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
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
        View infoContent = getLayoutInflater().inflate(R.layout.layout_mark_info, null);
        TextView tv_mark =  infoContent.findViewById(R.id.tv_mark);
        tv_mark.setText(marker.getTitle());
        return infoContent;
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
                                    BitmapDescriptorFactory.fromResource(R.drawable.ic_red_round)));
                        }
                        aMap.addPolyline(new PolylineOptions().
                                addAll(latLngs).width(20).color(Color.parseColor("#00EE00")));
                    }
                } else {
                    ToastUtil.show(this, R.string.no_result);
                }
            } else {
                ToastUtil.show(this, R.string.no_result);
            }
        } else {
            ToastUtil.show(this, rCode);
        }
    }
}
