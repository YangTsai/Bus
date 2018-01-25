package com.hyst.bus.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyst.bus.R;
import com.hyst.bus.activity.RoutePlanActivity;
import com.hyst.bus.activity.SetPointActivity;
import com.hyst.bus.constant.Constant;
import com.hyst.bus.model.event.SetPointEvent;

/**
 * Created by Administrator on 2018/1/16.
 */

public class RouteFragment extends BaseFragment {
    private TextView tv_start;
    private TextView tv_end;
    private SetPointEvent startPoint = null;
    private SetPointEvent endPoint = null;
    private ImageView iv_exchange;

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_route;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        tv_start = view.findViewById(R.id.tv_start);
        tv_end = view.findViewById(R.id.tv_end);
        iv_exchange = view.findViewById(R.id.iv_exchange);
        iv_exchange.setOnClickListener(this);
        tv_start.setOnClickListener(this);
        tv_end.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    public void setLocation(SetPointEvent event) {
        if (event.getType().equals(Constant.POINT_TYPE_START_VALUE)) {
            tv_start.setText(event.getContent());
            startPoint = event;
        } else if (event.getType().equals(Constant.POINT_TYPE_END_VALUE)) {
            tv_end.setText(event.getContent());
            endPoint = event;
        }
        if (!TextUtils.isEmpty(tv_start.getText().toString()) && !TextUtils.isEmpty(tv_end.getText().toString())
                && startPoint != null && endPoint != null) {
            Intent intent = new Intent(context, RoutePlanActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("startPoint", startPoint);
            bundle.putParcelable("endPoint", endPoint);
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_exchange:
                if (startPoint != null && endPoint != null) {
                    SetPointEvent event = startPoint;
                    startPoint = endPoint;
                    endPoint = event;
                    tv_start.setText(startPoint.getContent());
                    tv_end.setText(endPoint.getContent());
                    intent = new Intent(context, RoutePlanActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("startPoint", startPoint);
                    bundle.putParcelable("endPoint", endPoint);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            case R.id.tv_start:
                intent = new Intent(context, SetPointActivity.class);
                intent.putExtra(Constant.POINT_TYPE, Constant.POINT_TYPE_START_VALUE);
                intent.putExtra(Constant.POINT_TAG, "RouteFragment");
                startActivity(intent);
                break;
            case R.id.tv_end:
                intent = new Intent(context, SetPointActivity.class);
                intent.putExtra(Constant.POINT_TYPE, Constant.POINT_TYPE_END_VALUE);
                intent.putExtra(Constant.POINT_TAG, "RouteFragment");
                startActivity(intent);
                break;
        }
    }
}
