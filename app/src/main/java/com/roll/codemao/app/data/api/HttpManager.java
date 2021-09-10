package com.roll.codemao.app.data.api;

import android.content.Context;
import android.text.TextUtils;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.roll.codemao.BuildConfig;
import com.roll.codemao.base.di.LogInterceptor;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by LiuG on 2018/11/5.
 */

public class HttpManager {
    private static final int CONNECT_TIME_OUT = 20; //连接时间
    private static final int READ_TIME_OUT = 20;    //读取时间
    private static final int WRITE_TIME_OUT = 20;   //写入时间
    private static Context sContext;
    private static String sBaseURL;
    private static List<Interceptor> sInterceptors;
    private static boolean isInit = false;
    private Retrofit mRetrofit;
    private Map<Class<?>, Object> mMap;

    private static class SingletonInstance {
        private static final HttpManager INSTANCE = new HttpManager();
    }

    public static HttpManager getInstance() {
        return SingletonInstance.INSTANCE;
    }

    private HttpManager() {
        if (!isInit) {
            throw new RuntimeException("please invoke init() at first!");
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.SECONDS)
                .cache(getCache())
                // .addNetworkInterceptor(new CacheInterceptor())
                .cookieJar(new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(sContext)));
        addInterceptors(builder);
        mRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .baseUrl(sBaseURL)
                //addConverterFactory解析有先后顺序，按需添加设置
                .addConverterFactory(ScalarsConverterFactory.create())//转换为String对象
                .addConverterFactory(GsonConverterFactory.create())//转换为Gson对象
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//rxJava集成
                .build();
        mMap = new HashMap<>();
    }

    public static void init(Context context, String baseURL, Interceptor... interceptors) {
        if (isInit) {
            throw new RuntimeException("HttpManager already be inited!");
        }
        if (context == null) {
            throw new NullPointerException("context is null!");
        }
        if (TextUtils.isEmpty(baseURL)) {
            throw new NullPointerException("baseURL is empty!");
        }
        sContext = context.getApplicationContext();
        sBaseURL = baseURL;
        sInterceptors = Arrays.asList(interceptors);
        isInit = true;
    }

    /**
     * 缓存放在内部存储中，随应用卸载而删除，大小为10M。
     *
     * @return
     */
    private Cache getCache() {
        File file = new File(sContext.getCacheDir(), "response");
        Cache cache = new Cache(file, 20 * 1024 * 1024);
        return cache;
    }

    /**
     * 按需添加各种拦截器
     *
     * @param builder
     */
    private void addInterceptors(OkHttpClient.Builder builder) {
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(new LogInterceptor());//添加日志拦截器
            builder.addInterceptor(new StethoInterceptor());
        }
        if (sInterceptors == null || sInterceptors.size() == 0) {
            return;
        }
        for (Interceptor interceptor : sInterceptors) {
            builder.addInterceptor(interceptor);
        }
    }

    /**
     * 获取ApiService实例
     *
     * @param cls
     * @param <T>
     * @return
     */
    public <T> T create(Class<T> cls) {
        if (!mMap.containsKey(cls)) {
            T t = mRetrofit.create(cls);
            mMap.put(cls, t);
        }
        return (T) mMap.get(cls);
    }

}

