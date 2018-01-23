package com.hyst.bus.util;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.hyst.bus.R;
import com.hyst.bus.custom.DividerDecoration;


/**
 * Created by Administrator on 2017/7/7.
 */

public class ViewUtil {

    /**
     * 多行垂直滑动RecyclerView
     *
     * @param context
     * @param recyclerView
     * @param row
     * @return
     */
    public static RecyclerView getVRows(Context context, RecyclerView recyclerView, int row) {
        //设置layoutManager
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(row, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new DividerDecoration(context, R.color.line, 1));
        //设置Item增加、移除动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return recyclerView;
    }

    public static RecyclerView getVRowsNoLine(Context context, RecyclerView recyclerView, int row) {
        //设置layoutManager
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(row, StaggeredGridLayoutManager.VERTICAL));
        //设置Item增加、移除动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return recyclerView;
    }

    /**
     * 多行水平滑动RecyclerView
     *
     * @param context
     * @param recyclerView
     * @param row
     * @return
     */
    public static RecyclerView getHRows(Context context, RecyclerView recyclerView, int row) {
        //设置layoutManager
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(row, StaggeredGridLayoutManager.HORIZONTAL));
        recyclerView.addItemDecoration(new DividerDecoration(context, R.color.white, 1));
        //设置Item增加、移除动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return recyclerView;
    }
    /**
     * 多行水平滑动RecyclerView
     *
     * @param context
     * @param recyclerView
     * @param row
     * @return
     */
    public static RecyclerView getHRowsNoLine(Context context, RecyclerView recyclerView, int row) {
        //设置layoutManager
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(row, StaggeredGridLayoutManager.HORIZONTAL));
        //设置Item增加、移除动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        return recyclerView;
    }
}
