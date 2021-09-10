package com.roll.codemao.base.di;

import android.util.MalformedJsonException;

import com.google.gson.JsonSyntaxException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;


/**
 * Created by wjn on 2016/7/4.
 * 自定义网络请求观察者
 */
public abstract class Callback<T> {
    public static final int TYPE_NETWORK_ERROR = 1;
    public static final int TYPE_PARSE_ERROR = 2;
    public static final int TYPE_OTHER_ERROR = 3;
    public static final int TYPE_API_ERROR = 4;

    public void onError(Throwable e) {
        if (e instanceof SocketTimeoutException || e instanceof ConnectException || e instanceof UnknownHostException
        ) {
            e.printStackTrace();
            onError(e, TYPE_NETWORK_ERROR);
        } else if (e instanceof MalformedJsonException || e instanceof JsonSyntaxException) {
            e.printStackTrace();
            onError(e, TYPE_PARSE_ERROR);
        } else if (e instanceof HttpException) {
            e.printStackTrace();
            onError(e, TYPE_API_ERROR);
        } else {
            onError(e, TYPE_OTHER_ERROR);
            e.printStackTrace();
        }
    }

    public abstract void onData(T t);

    public void onError(Throwable e, int type) {

    }

    public void onFail(T t) {

    }

}
