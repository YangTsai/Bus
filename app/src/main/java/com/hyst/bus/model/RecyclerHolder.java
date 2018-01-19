package com.hyst.bus.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyst.bus.R;


/**
 * 类注释
 *
 * @author Yann
 * @date 2015-8-5 下午9:08:31
 */
public class RecyclerHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;
    private View mConvertView;
    private Context mContext;

    public RecyclerHolder(Context context, View itemView, ViewGroup parent) {
        super(itemView);
        mContext = context;
        mConvertView = itemView;
        mViews = new SparseArray<>();
    }


    public static RecyclerHolder get(Context context, ViewGroup parent, int layoutId) {

        View itemView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        RecyclerHolder holder = new RecyclerHolder(context, itemView, parent);
        return holder;
    }


    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * @param viewId
     * @return
     */
    public RecyclerHolder setImageGlide(int viewId, String url) {
        ImageView iv = getView(viewId);
        Glide.with(mContext)
                .load(url)
                .crossFade()
                .placeholder(R.drawable.ic_launcher)
                .error(R.drawable.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv);
        return this;
    }

    public RecyclerHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public RecyclerHolder setCheck(int viewId, boolean bool) {
        CheckBox tv = getView(viewId);
        tv.setChecked(bool);
        return this;
    }

    public RecyclerHolder setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    public RecyclerHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public RecyclerHolder setOnCheckedChangeListener(int viewId, CompoundButton.OnCheckedChangeListener listener) {
        CheckBox view = getView(viewId);
        view.setOnCheckedChangeListener(listener);
        return this;
    }

}