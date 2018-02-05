package com.hyst.bus.util;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressUtil {

    private static ProgressDialog progressDialog;
    private static final String TITLE = "提示";

    public static void showDlg(Context context, String info) {
        progressDialog = new ProgressDialog(context);
//		progressDialog.setTitle(TITLE);
        progressDialog.setMessage(info);
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

    }

    public static void cancleDlg() {

        if (null != progressDialog && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
