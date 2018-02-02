package com.hyst.bus.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.hyst.bus.R;
import com.hyst.bus.adapter.RecyclerAdapter;
import com.hyst.bus.constant.Constant;
import com.hyst.bus.model.RecyclerHolder;
import com.hyst.bus.model.cache.LocationCache;
import com.hyst.bus.model.event.SetPointEvent;
import com.hyst.bus.util.LocationUtil;
import com.hyst.bus.util.ViewUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/16.
 */

public class SetPointActivity extends BaseActivity implements PoiSearch.OnPoiSearchListener {

    private ImageView iv_back;
    private EditText et_location;
    private ImageView iv_clear;
    private TextView tv_location;

    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private List<PoiItem> data;
    //
    private String pointType;
    private String tag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_set_point;
    }

    @Override
    protected void initView() {
        et_location = (EditText) findViewById(R.id.et_location);
        iv_clear = (ImageView) findViewById(R.id.iv_clear);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_location = (TextView) findViewById(R.id.tv_location);
        iv_back.setOnClickListener(this);
        tv_location.setOnClickListener(this);
        iv_clear.setOnClickListener(this);
        final LocationCache locationCache = LocationUtil.getIns(this).getCurrentLocation();
        et_location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int length = editable.toString().length();
                if (length > 0) {
                    iv_clear.setVisibility(View.VISIBLE);
                    setSearch(editable.toString(), locationCache.getCityName());
                } else {
                    iv_clear.setVisibility(View.GONE);
                    adapter.setDatas(null);
                }
            }
        });
    }

    @Override
    protected void initData() {
        pointType = getIntent().getStringExtra(Constant.POINT_TYPE);
        tag = getIntent().getStringExtra(Constant.POINT_TAG);
        et_location.setHint(pointType);
        //
        data = new ArrayList<>();
        adapter = new RecyclerAdapter<PoiItem>(this, data, R.layout.item_set_point) {
            @Override
            public void convert(RecyclerHolder holder, final PoiItem poiItem) {
                holder.setText(R.id.tv_name, poiItem.getTitle());
                holder.setText(R.id.tv_address, poiItem.getSnippet());
                holder.setOnClickListener(R.id.ll_point, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EventBus.getDefault().post(new SetPointEvent(tag, pointType, poiItem.getTitle(), poiItem.getLatLonPoint()));
                        finish();
                    }
                });
            }
        };
        recyclerView = ViewUtil.getVRows(this, recyclerView, 1);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Poi检索
     *
     * @param keyWord 关键字
     * @param city    城市名或编码
     */
    private void setSearch(String keyWord, String city) {
        PoiSearch.Query query = new PoiSearch.Query(keyWord, "", city);
        query.setPageNum(1);//设置查询页码
        PoiSearch poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_clear:
                et_location.setText("");
                break;
            case R.id.tv_location:
                LocationUtil.getIns(this).setLocation(getClass().getName(),true);
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SetPointEvent event) {
        if (event != null && event.getTag().equals(getClass().getName())) {
            et_location.setText("我的位置");
            EventBus.getDefault().post(new SetPointEvent(tag, pointType, "我的位置", event.getLatLonPoint()));
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        if (i == 1000 && poiResult != null) {
            data = poiResult.getPois();
            adapter.setDatas(data);
        }
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

}
