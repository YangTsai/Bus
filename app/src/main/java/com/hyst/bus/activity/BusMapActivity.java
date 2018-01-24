package com.hyst.bus.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.hyst.bus.R;

/**
 * Created by Administrator on 2018/1/24.
 */

public class BusMapActivity extends BaseActivity implements AMap.OnMapLoadedListener {

    private MapView mapView;
    private AMap aMap;
    private ImageView iv_back;
    private Bundle savedInstanceState;

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
        aMap.setOnMapLoadedListener(this);
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
}
