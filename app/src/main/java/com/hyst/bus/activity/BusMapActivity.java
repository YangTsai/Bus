package com.hyst.bus.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.busline.BusLineItem;
import com.hyst.bus.R;
import com.hyst.bus.custom.BusLineOverlay;
import com.hyst.bus.model.cache.LocationCache;
import com.hyst.bus.util.LocationUtil;

/**
 * Created by Administrator on 2018/1/24.
 */

public class BusMapActivity extends BaseActivity implements AMap.OnMapLoadedListener,
        AMap.OnMapClickListener, AMap.InfoWindowAdapter {

    private MapView mapView;
    private AMap aMap;
    private ImageView iv_back;
    private Bundle savedInstanceState;
    //

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
        iv_back = (ImageView) findViewById(R.id.iv_back);
        mapView = (MapView) findViewById(R.id.map_bus);
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

    }

    @Override
    public void onMapLoaded() {
        BusLineItem busLineItem = getIntent().getParcelableExtra("bus");
        LocationCache locationCache = LocationUtil.getIns(this).getCurrentLocation();
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(locationCache.getLatitude(), locationCache.getLongitude())));
        BusLineOverlay busLineOverlay = new BusLineOverlay(this, aMap, busLineItem);
        busLineOverlay.removeFromMap();
        busLineOverlay.addToMap();
        busLineOverlay.zoomToSpan();
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
        TextView tv_mark = (TextView) infoContent.findViewById(R.id.tv_mark);
        tv_mark.setText(marker.getTitle());
        return infoContent;
    }
}
