package com.hyst.bus.activity;

import android.content.Intent;
import com.hyst.bus.R;

/**
 * Created by Administrator on 2018/1/10.
 */

public class StartActivity extends BaseActivity {

    @Override
    protected int setLayoutId() {
        return R.layout.activity_start;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
