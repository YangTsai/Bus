package com.hyst.bus.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.busline.BusLineItem;
import com.amap.api.services.busline.BusLineQuery;
import com.amap.api.services.busline.BusLineResult;
import com.amap.api.services.busline.BusLineSearch;
import com.amap.api.services.core.AMapException;
import com.hyst.bus.R;
import com.hyst.bus.custom.BusLineOverlay;
import com.hyst.bus.model.cache.LocationCache;
import com.hyst.bus.model.event.SetPointEvent;
import com.hyst.bus.util.LocationUtil;
import com.hyst.bus.util.ToastUtil;

import java.util.List;

/**
 * Created by Administrator on 2018/1/16.
 */

public class MapFragment extends BaseFragment implements BusLineSearch.OnBusLineSearchListener,
        AMap.OnMapClickListener, AMap.OnMapLoadedListener, AMap.InfoWindowAdapter {
    private MapView mapView;
    private AMap aMap;
    private EditText et_search;
    private TextView tv_search;
    //
    private BusLineQuery busLineQuery;
    private BusLineSearch busLineSearch;

    private Marker mMarker;

    private LatLng mLatLng;

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
        aMap.setOnMapLoadedListener(this);
    }

    public void setLocation(SetPointEvent event) {
        if (event.getLatLonPoint() == null) {
            LocationCache locationCache = LocationUtil.getIns(context).getCurrentLocation();
            mLatLng = new LatLng(locationCache.getLatitude(), locationCache.getLongitude());
            busLineQuery = new BusLineQuery("", BusLineQuery.SearchType.BY_LINE_NAME, locationCache.getCityName());
        } else {
            mLatLng = new LatLng(event.getLatLonPoint().getLatitude(), event.getLatLonPoint().getLongitude());
            busLineQuery = new BusLineQuery("", BusLineQuery.SearchType.BY_LINE_NAME, event.getaMapLocation().getCity());
        }
        busLineQuery.setPageSize(10);
        busLineQuery.setPageNumber(1);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_search:
                if (!TextUtils.isEmpty(et_search.getText().toString())) {
                    busLineQuery.setQueryString(et_search.getText().toString());
                    busLineSearch = new BusLineSearch(context, busLineQuery);
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
        aMap.clear();
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getQuery() != null
                    && result.getQuery().equals(busLineQuery)) {
                if (result.getQuery().getCategory() == BusLineQuery.SearchType.BY_LINE_NAME) {
                    if (result.getPageCount() > 0 && result.getBusLines() != null
                            && result.getBusLines().size() > 0) {
                        List<BusLineItem> lines = result.getBusLines();
                        BusLineOverlay busLineOverlay = new BusLineOverlay(context, aMap, lines.get(0));
                        busLineOverlay.removeFromMap();
                        busLineOverlay.addToMap();
                        busLineOverlay.zoomToSpan();
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
    public void onMapLoaded() {
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(mLatLng));
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

}
