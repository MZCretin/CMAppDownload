package com.roll.codemao.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import androidx.annotation.ArrayRes;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.Utils;

import java.util.Locale;

public class ResUtils {

    @NonNull
    private static Context getContext() {
        return Utils.getApp();
    }

    /**
     * 得到Resouce对象
     */
    public static Resources getResource() {
        Activity activity = ActivityUtils.getTopActivity();
        if (activity != null) {
            return activity.getResources();
        }
        return getContext().getResources();
    }

    /**
     * 得到String.xml中的字符串
     */
    public static String getString(@StringRes int resId) {
        return getResource().getString(resId);
    }

    /**
     * 得到String.xml中的字符串,带占位符
     */
    public static String getString(@StringRes int id, Object... formatArgs) {
        return getResource().getString(id, formatArgs);
    }

    /**
     * 格式 String 字符版本
     */
    public static String getString(String format, Object... formatArgs) {
        return String.format(Locale.getDefault(), format, formatArgs);
    }

    /**
     * array.xml中的字符串数组
     */
    public static String[] getStringArr(@ArrayRes int resId) {
        return getResource().getStringArray(resId);
    }


    /**
     * array.xml中的数组
     */
    public static int[] getIntArr(@ArrayRes int resId) {
        return getResource().getIntArray(resId);
    }

    public static float getDimension(@DimenRes int demenId) {
        return getResource().getDimension(demenId);
    }

    public static int getDimensionPixelOffset(@DimenRes int demenId) {
        return getResource().getDimensionPixelOffset(demenId);
    }

    /**
     * 得到colors.xml中的颜色
     */
    @ColorInt
    public static int getColor(@ColorRes int colorId) {
        return ContextCompat.getColor(getContext(), colorId);
    }


    /**
     * 获取 drawable
     *
     * @param resId 资源 id
     * @return Drawable
     */
    public static Drawable getDrawable(@DrawableRes int resId) {
        return getResource().getDrawable(resId);
    }

    public static Configuration getConfiguration() {
        return getResource().getConfiguration();
    }

}
