package com.hyst.bus.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 基准懒加载Fragment
 */

public abstract class BaseLazyFragment extends BaseFragment{
    //Fragment的View加载完毕的标记
    private boolean isViewCreated;
    //Fragment对用户可见的标记
    private boolean isUIVisible;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(setLayoutId(), container, false);
        initView(view, savedInstanceState);
        initData();
        isViewCreated = true;
        lazyLoad();
        return view;
    }

    /**
     * 加载数据
     */
    protected abstract void loadData();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //isVisibleToUser这个boolean值表示:该Fragment的UI 用户是否可见
        if (isVisibleToUser) {
            isUIVisible = true;
            lazyLoad();
        } else {
            isUIVisible = false;
        }
    }

    /**
     * 懒加载
     */
    private void lazyLoad() {
        //这里进行双重标记判断,是因为setUserVisibleHint会多次回调,并且会在onCreateView执行前回调,必须确保onCreateView加载完毕且页面可见,才加载数据
        if (isViewCreated && isUIVisible) {
            loadData();
            //数据加载完毕,恢复标记,防止重复加载
            isViewCreated = false;
            isUIVisible = false;
        }
    }
}
