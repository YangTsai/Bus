package com.hyst.bus.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hyst.bus.R;
import com.hyst.bus.fragment.HomeFragment;
import com.hyst.bus.fragment.MapFragment;
import com.hyst.bus.fragment.RouteFragment;
import com.hyst.bus.model.event.SetPointEvent;
import com.hyst.bus.util.AppManager;
import com.hyst.bus.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2017/8/25.
 */

public class MainActivity extends BaseActivity {
    private boolean isExit;
    //   为每个fragment设置标签
    private LinearLayout ll_home;
    private LinearLayout ll_route;
    private LinearLayout ll_map;
    private ImageView iv_home, iv_route, iv_map;
    private TextView tv_home, tv_route, tv_map;

    private HomeFragment homeFragment;
    private RouteFragment routeFragment;
    private MapFragment mapFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        homeFragment = new HomeFragment();
        routeFragment = new RouteFragment();
        mapFragment = new MapFragment();
        setFragment(homeFragment);
        setBackGround(0);
    }

    @Override
    protected void initView() {
        ll_home = (LinearLayout) findViewById(R.id.ll_home);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        tv_home = (TextView) findViewById(R.id.tv_home);
        ll_home.setOnClickListener(this);
        ll_route = (LinearLayout) findViewById(R.id.ll_route);
        iv_route = (ImageView) findViewById(R.id.iv_route);
        tv_route = (TextView) findViewById(R.id.tv_route);
        ll_route.setOnClickListener(this);
        ll_map = (LinearLayout) findViewById(R.id.ll_map);
        iv_map = (ImageView) findViewById(R.id.iv_map);
        tv_map = (TextView) findViewById(R.id.tv_map);
        ll_map.setOnClickListener(this);
    }


    private void setBackGround(int position) {
        //
        iv_home.setImageResource(R.drawable.home_off);
        iv_route.setImageResource(R.drawable.route_off);
        iv_map.setImageResource(R.drawable.map_off);
        //
        tv_home.setTextColor(getResources().getColor(R.color.colorGray));
        tv_route.setTextColor(getResources().getColor(R.color.colorGray));
        tv_map.setTextColor(getResources().getColor(R.color.colorGray));
        if (position == 0) {
            iv_home.setImageResource(R.drawable.home_on);
            tv_home.setTextColor(getResources().getColor(R.color.colorTheme));
        } else if (position == 1) {
            iv_route.setImageResource(R.drawable.route_on);
            tv_route.setTextColor(getResources().getColor(R.color.colorTheme));
        } else if (position == 2) {
            iv_map.setImageResource(R.drawable.map_on);
            tv_map.setTextColor(getResources().getColor(R.color.colorTheme));
        }
    }


    private void setFragment(Fragment currentFragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.hide(homeFragment).hide(routeFragment).hide(mapFragment);
        if (currentFragment.isAdded()) {
            transaction.show(currentFragment);
        } else {
            transaction.add(R.id.ll_fragment, currentFragment);
            transaction.show(currentFragment);
        }
        transaction.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_home:
                setFragment(homeFragment);
                setBackGround(0);
                break;
            case R.id.ll_route:
                setFragment(routeFragment);
                setBackGround(1);
                break;
            case R.id.ll_map:
                setFragment(mapFragment);
                setBackGround(2);
                break;

        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SetPointEvent event) {
        if (event != null && event.getTag().equals(routeFragment.getClass().getName())) {
            routeFragment.setLocation(event);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();
        }
        return false;
    }

    /**
     * 双击退出函数
     */

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true;
            ToastUtil.show(this, "再按一次退出程序");
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            AppManager.getInstance().exit();
        }
    }


}
