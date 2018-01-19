package com.hyst.bus.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 基准Fragment
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener{
    protected Context context;
    protected Activity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(setLayoutId(), container, false);
        initView(view, savedInstanceState);
        initData();
        return view;
    }

    /**
     * 设置布局Id
     *
     * @return
     */
    protected abstract int setLayoutId();

    /**
     * 初始化view
     */
    protected abstract void initView(View view, Bundle savedInstanceState);

    /**
     * 初始化data
     */
    protected abstract void initData();

    @Override
    public void onClick(View view) {

    }
}
