package com.hyst.bus.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.hyst.bus.R;

/**
 * Created by Administrator on 2018/1/16.
 */

public class MapFragment extends BaseFragment{
    private MapView mapView;
    private AMap aMap;
    private EditText et_search;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_map;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        et_search =  view.findViewById(R.id.et_search);
        mapView =  view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
//                if (!TextUtils.isEmpty(et_search.getText().toString())) {
//                    Intent intent = new Intent(context, StationDetailActivity.class);
//                    intent.putExtra("bus", et_search.getText().toString());
//                    startActivity(intent);
//                } else {
//                    ToastUtil.show(context, "请输入公交号");
//                }
            }
        });
    }

    @Override
    protected void initData() {
        aMap = mapView.getMap();
        aMap.getUiSettings().setZoomControlsEnabled(false);
//        aMap.moveCamera(CameraUpdateFactory.changeLatLng(Constants.XIAN));
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
