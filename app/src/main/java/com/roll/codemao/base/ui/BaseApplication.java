package com.roll.codemao.base.ui;

import android.app.Application;
import android.content.Context;
import androidx.multidex.MultiDex;

import com.blankj.utilcode.util.Utils;
import com.roll.codemao.app.data.api.ApiConfig;
import com.roll.codemao.app.data.api.HttpManager;
import com.roll.codemao.base.di.CookieInterceptor;
import com.roll.codemao.base.ui.helper.GlideImageEngine;
import com.roll.codemao.base.ui.helper.ImageHelper;
import com.facebook.stetho.Stetho;
import com.orhanobut.hawk.Hawk;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.plugins.RxJavaPlugins;

/**
 * Created by cretin on 2018/3/27.
 */
public class BaseApplication extends Application   {
    private static BaseApplication application;

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;

        //初始化Blankj的工具
        Utils.init(this);

        //初始化Hawk
        initHawk();

        initHttp();

        JodaTimeAndroid.init(this);

        Stetho.initializeWithDefaults(this);

        //设置图片加载框架
        ImageHelper.INSTANCE.bindImageEngine(new GlideImageEngine());

        //index加速
//        EventBus.builder().addIndex(new MyEventBusIndex()).installDefaultEventBus();
    }

    private void initHttp() {
        HttpManager.init(application, ApiConfig.BASE_URL, new CookieInterceptor()/*, HostInterceptor()*/);//去掉动态域名配置项
        // RxJava error handler
        setRxJavaErrorHandler();
    }

    /**
     * Fix tingyun bug id 78465913:
     * Caused by: java.net.UnknownHostException bug.
     */
    private void setRxJavaErrorHandler() {
        RxJavaPlugins.setErrorHandler(throwable -> {
            try {
                Map<String,String> map = new HashMap<>();
                map.put("error",throwable.getLocalizedMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    //手动配置Hawk
    private void initHawk() {
        Hawk.init(this)
                .build();
    }

    public static BaseApplication getApp() {
        return application;
    }

    public static BaseApplication from(Context context) {
        return (BaseApplication) context.getApplicationContext();
    }
}
