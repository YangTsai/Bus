package com.hyst.bus.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.hyst.bus.R;
import com.hyst.bus.model.cache.LocationCache;
import com.hyst.bus.model.event.PoiItemEvent;
import com.hyst.bus.util.LocationUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2018/1/24.
 */

public class MapPointActivity extends BaseActivity implements AMap.OnMapLoadedListener,
        AMap.OnCameraChangeListener, PoiSearch.OnPoiSearchListener {

    private MapView mapView;
    private AMap aMap;
    private ImageView iv_back;
    private Bundle savedInstanceState;
    //
    private PoiSearch.Query query;
    private PoiSearch poiSearch;
    private TextView tv_name;
    private TextView tv_address;
    private TextView tv_sure;

    private PoiItem mPoiItem;
    private String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_map_location;
    }

    @Override
    protected void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        mapView = (MapView) findViewById(R.id.map_bus);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_sure = (TextView) findViewById(R.id.tv_sure);

        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();
        aMap.setOnMapLoadedListener(this);
        aMap.setOnCameraChangeListener(this);//设置监听方法
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new PoiItemEvent(tag, mPoiItem));
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        tag = getIntent().getStringExtra("tag");
    }

    @Override
    public void onMapLoaded() {
        LocationCache locationCache = LocationUtil.getIns(this).getCurrentLocation();
        aMap.moveCamera(CameraUpdateFactory.zoomTo(14));
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(locationCache.getLatitude(), locationCache.getLongitude())));
        query = new PoiSearch.Query("", "", locationCache.getCityName());
        query.setPageNum(1);//设置查询页码
        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
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
    public void onPoiSearched(PoiResult poiResult, int i) {
        if (i == 1000 && poiResult != null) {
            if (poiResult.getPois() != null && poiResult.getPois().size() > 0) {
                mPoiItem = poiResult.getPois().get(0);
                tv_name.setText(poiResult.getPois().get(0).getTitle());
                tv_address.setText(poiResult.getPois().get(0).getSnippet());
            }
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        LatLng latLng = cameraPosition.target;
        LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
        poiSearch.setBound(new PoiSearch.SearchBound(latLonPoint, 500, true));
        poiSearch.searchPOIAsyn();
    }
}
