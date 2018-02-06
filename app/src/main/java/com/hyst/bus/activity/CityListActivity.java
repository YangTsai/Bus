package com.hyst.bus.activity;

import android.widget.TextView;

import com.hyst.bus.R;
import com.hyst.bus.custom.MyLetterView;

/**
 * Created by Administrator on 2018/2/2.
 */

public class CityListActivity extends BaseActivity {
    private MyLetterView myLetterView;
    private TextView tvDialog;
    @Override
    protected int setLayoutId() {
        return R.layout.activity_city_list;
    }

    @Override
    protected void initView() {
        myLetterView=(MyLetterView) findViewById(R.id.my_letterview);
        tvDialog=(TextView) findViewById(R.id.tv_dialog);
        //将中间展示字母的TextView传递到myLetterView中并在其中控制它的显示与隐藏
        myLetterView.setTextView(tvDialog);
        //注册MyLetterView中监听(跟setOnClickListener这种系统默认写好的监听一样只不过这里是我们自己写的)
        myLetterView.setOnSlidingListener(new MyLetterView.OnSlidingListener() {

            @Override
            public void sliding(String str) {
                tvDialog.setText(str);
            }
        });
    }

    @Override
    protected void initData() {

    }
}
