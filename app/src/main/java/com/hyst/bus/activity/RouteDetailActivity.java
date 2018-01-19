package com.hyst.bus.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.hyst.bus.R;
import com.hyst.bus.adapter.FragmentAdapter;
import com.hyst.bus.custom.BusRouteOverlay;
import com.hyst.bus.fragment.routeplan.RoutePlanFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/17.
 */

public class RouteDetailActivity extends BaseActivity implements AMap.OnMapLoadedListener {
    private BusRouteOverlay mBusrouteOverlay;
    private MapView mapView;
    private AMap aMap;
    private ImageView iv_back;
    private Bundle savedInstanceState;
    private BusPath busPath;
    private BusRouteResult mBusRouteResult;
    //
    private ViewPager viewPager;
    private List<Fragment> fragments;
    private FragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_route_detail;
    }

    @Override
    protected void initView() {
        mapView = findViewById(R.id.route_map);
        iv_back = findViewById(R.id.iv_back);
        viewPager = findViewById(R.id.viewPager);
        mapView.onCreate(savedInstanceState);
        iv_back.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        busPath = getIntent().getParcelableExtra("path");
        mBusRouteResult = getIntent().getParcelableExtra("result");
        aMap = mapView.getMap();
        aMap.setOnMapLoadedListener(this);
        aMap.getUiSettings().setZoomControlsEnabled(false);
        setBottomSheet();
        setFragment();
    }

    private void setFragment() {
        fragments = new ArrayList<>();
        int currentPosition = 0;
        for (int i = 0; i < mBusRouteResult.getPaths().size(); i++) {
            if (busPath.getDuration() == mBusRouteResult.getPaths().get(i).getDuration()) {
                currentPosition = i;
            }
            RoutePlanFragment fragment = new RoutePlanFragment();
            fragment.setData(mBusRouteResult.getPaths().get(i), i, mBusRouteResult.getPaths().size());
            fragments.add(fragment);
        }
        adapter = new FragmentAdapter(getSupportFragmentManager(), fragments);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(currentPosition, false);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setMapData(mBusRouteResult.getPaths().get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setMapData(mBusRouteResult.getPaths().get(currentPosition));
    }


    private void setBottomSheet() {
        CoordinatorLayout coordinatorLayout = findViewById(R.id.cl_bottom);
        View bottomSheet = coordinatorLayout.findViewById(R.id.bottom_sheet);
        final BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setPeekHeight(900);
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                //这里是bottomSheet 状态的改变，根据slideOffset可以做一些动画
                behavior.setPeekHeight(320);
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                //这里是拖拽中的回调，根据slideOffset可以做一些动画
            }
        });
    }

    public void setMapData(BusPath busPath) {
        aMap.clear();// 清理地图上的所有覆盖物
        mBusrouteOverlay = new BusRouteOverlay(this, aMap, busPath, mBusRouteResult.getStartPos(),
                mBusRouteResult.getTargetPos());
        mBusrouteOverlay.removeFromMap();
        mBusrouteOverlay.addToMap();
        mBusrouteOverlay.zoomToSpan();

    }

    @Override
    public void onMapLoaded() {
        if (mBusrouteOverlay != null) {
            mBusrouteOverlay.addToMap();
            mBusrouteOverlay.zoomToSpan();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
