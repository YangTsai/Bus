package com.hyst.bus.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.hyst.bus.R;
import com.hyst.bus.util.LocationUtil;

/**
 * Created by Administrator on 2018/1/10.
 */

public class StartActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        LocationUtil.getIns(this).setLocation(getClass().getName(), false);
        new Handler().postAtTime(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(StartActivity.this, MainActivity.class));
                finish();
            }
        }, 1000);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
