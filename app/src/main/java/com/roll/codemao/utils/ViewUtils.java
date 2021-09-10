package com.roll.codemao.utils;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.TextView;


import com.roll.codemao.app.KV;
import com.roll.codemao.app.LocalStorageKeys;

import java.lang.reflect.Field;

/**
 * Created by cretin on 16/8/8.
 */
public class ViewUtils {
    //状态栏的高度
    public static int mStatusBarHeight;
    //屏幕高度
    public static int mWindowHeight;
    //屏幕宽度
    public static int mWindowWidth;

    /**
     * 获取状态栏的高度
     */
    public static int getStatusBarHeights(Context context) {
        if ( mStatusBarHeight == 0 ) {
            Class<?> c = null;
            Object obj = null;
            Field field = null;
            int x = 0;
            int statusBarHeight = KV.get(LocalStorageKeys.APP_STATUS_HEIGHT, 0);
            if ( statusBarHeight == 0 )
                try {
                    c = Class.forName("com.android.internal.R$dimen");
                    obj = c.newInstance();
                    field = c.getField("status_bar_height");
                    x = Integer.parseInt(field.get(obj).toString());
                    statusBarHeight = context.getResources().getDimensionPixelSize(x);
                    KV.put(LocalStorageKeys.APP_STATUS_HEIGHT, statusBarHeight);
                } catch ( Exception e1 ) {
                    e1.printStackTrace();
                }
            mStatusBarHeight = statusBarHeight;
        }
        return mStatusBarHeight;
    }

    //获取屏幕高度
    public static int getWindowHeight(Context context) {
        if ( mWindowHeight == 0 ) {
            int height = KV.get(LocalStorageKeys.APP_WINDOW_HEIGHT, 0);
            if ( height != 0 ) {
                return height;
            }
            DisplayMetrics dm = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
            int width = dm.widthPixels;//宽度
            height = dm.heightPixels;//高度
            KV.put(LocalStorageKeys.APP_WINDOW_WIDTH, width);
            KV.put(LocalStorageKeys.APP_WINDOW_HEIGHT, height);
            mWindowHeight = height;
        }
        return mWindowHeight;
    }

    //获取屏幕宽度
    public static int getWindowWidth(Context context) {
        if ( mWindowWidth == 0 ) {
            int width = KV.get(LocalStorageKeys.APP_WINDOW_WIDTH, 0);
            if ( width != 0 ) {
                return width;
            }
            DisplayMetrics dm = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
            width = dm.widthPixels;//宽度
            int height = dm.heightPixels;//高度
            KV.put(LocalStorageKeys.APP_WINDOW_WIDTH, width);
            KV.put(LocalStorageKeys.APP_WINDOW_HEIGHT, height);
            mWindowWidth = width;
        }
        return mWindowWidth;
    }

    private static TextView tv;

    /**
     * 获取屏幕的缩放级别
     * @param context
     * @return
     */
    public static float getScreenScale(Context context) {
        if ( tv == null ) {
            tv = new TextView(context);
            tv.setTextSize(1);
        }
        return tv.getTextSize();
    }

}
