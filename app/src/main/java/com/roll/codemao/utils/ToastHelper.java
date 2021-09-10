package com.roll.codemao.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.widget.Toast;

import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.Utils;

public class ToastHelper {
    public static void show(String message) {
        if ( !TextUtils.isEmpty(message) ){
            Toast.makeText(Utils.getApp(), message, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showMsgAnywhere(String message){
        ThreadUtils.runOnUiThread(new Runnable() {
            @SuppressLint("ShowToast")
            @Override
            public void run() {
                Toast.makeText(Utils.getApp(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
