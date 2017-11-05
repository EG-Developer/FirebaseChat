package com.example.kyung.firebasechat.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.example.kyung.firebasechat.R;

/**
 * Created by Kyung on 2017-11-04.
 */

public class DialogUtil {
    public static void showDialog(final Activity activity, String msg, final boolean activityFinish){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        dialogBuilder
                .setTitle(activity.getString(R.string.alert_notice))
                .setMessage(msg)
                .setCancelable(true)
                .setPositiveButton(activity.getString(R.string.alert_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if(activityFinish){
                            activity.finish();
                        }
                    }
                });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }
}
