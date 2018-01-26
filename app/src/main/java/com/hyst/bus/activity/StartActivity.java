package com.hyst.bus.activity;

import android.content.Intent;
import android.os.Bundle;

import com.hyst.bus.R;
import com.hyst.bus.model.event.SetPointEvent;
import com.hyst.bus.util.LocationUtil;
import com.hyst.bus.util.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Administrator on 2018/1/10.
 */

public class StartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_start;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        LocationUtil.setLocation(this, "StartActivity");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SetPointEvent event) {
        if (event != null && event.getTag().equals("StartActivity")) {
            startActivity(new Intent(this, MainActivity.class));
        } else if (event != null && event.getTag() == null) {
            ToastUtil.show(this, "不打开定位，则无法正常使用");
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
