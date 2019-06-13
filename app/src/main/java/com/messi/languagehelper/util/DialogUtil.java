package com.messi.languagehelper.util;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.webkit.SslErrorHandler;

public class DialogUtil {

    public static void OnReceivedSslError(Context context, SslErrorHandler handler) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String message = "SSL Certificate error.";
        message += " Do you want to continue anyway?";
        builder.setTitle("SSL Certificate Error");
        builder.setMessage(message);
        builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.proceed();
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                handler.cancel();
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
    }

}
