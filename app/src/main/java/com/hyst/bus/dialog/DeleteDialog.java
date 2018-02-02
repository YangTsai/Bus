package com.hyst.bus.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.hyst.bus.R;
import com.hyst.bus.fragment.HomeFragment;
import com.hyst.bus.fragment.RouteFragment;
import com.hyst.bus.util.BusCacheUtil;
import com.hyst.bus.util.RouteCacheUtil;


/**
 * 提示
 */
public class DeleteDialog implements OnClickListener {
    private Context context;
    private Dialog dialog;
    private TextView tv_tip_sure;
    private TextView tv_tip_cancel;
    private String type;
    private Fragment fragment;

    public DeleteDialog(Context context) {
        this.context = context;
        initView();
    }

    private void initView() {
        dialog = BaseDialog.getIntence(context).getDialog(R.layout.dialog_delete,
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        tv_tip_sure = (TextView) dialog.findViewById(R.id.tv_tip_sure);
        tv_tip_cancel = (TextView) dialog.findViewById(R.id.tv_tip_cancel);
        tv_tip_sure.setOnClickListener(this);
        tv_tip_cancel.setOnClickListener(this);
    }

    public void showDialog(String type, Fragment fragment) {
        this.type = type;
        this.fragment = fragment;
        dialog.show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_tip_sure:
                if(type.equals("BusCache")){
                    BusCacheUtil.clearCache(context);
                    ((HomeFragment)fragment).delete();
                }else if(type.equals("RouteCache")){
                    RouteCacheUtil.clearCache(context);
                    ((RouteFragment)fragment).delete();
                }
                dialog.dismiss();
                break;
            case R.id.tv_tip_cancel:
                dialog.dismiss();
                break;
            default:
                break;
        }
    }

}
