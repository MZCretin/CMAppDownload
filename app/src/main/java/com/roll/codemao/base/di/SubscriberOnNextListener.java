package com.roll.codemao.base.di;

/**
 * Created by wjn on 2016/7/4.
 *   获取model数据监听
 */
public interface SubscriberOnNextListener<T> {
    void onNext(T t);
    void onCompleted();
    void onError(Throwable e, int type);
}
