package com.roll.codemao.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.ObservableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Administrator on 2016/11/29.
 */

public class RxUtils {
    private static Scheduler schedulerIO;
    private static Scheduler schedulerComputation;

    public static void init() {
        ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(10);
        schedulerIO = Schedulers.from(threadPoolExecutor);
        int threadCount = Runtime.getRuntime().availableProcessors();
        ExecutorService processorThreadPoolExecutor = Executors.newFixedThreadPool(threadCount);
        schedulerComputation = Schedulers.from(processorThreadPoolExecutor);
    }

    /**
     * 将数据集拆分成子集并指派给规定数量的线程，并传入回调来进行具体业务逻辑处理。
     * <b>T</b> 是要被处理的数据类型，<b>U</b> 是返回的数据类型
     */
    public static Scheduler getProcessorSchedulers() {
        if (schedulerComputation == null) {
            init();
        }
        return schedulerComputation;
    }

    public static Scheduler getSchedulerIO() {
        if (schedulerIO == null) {
            init();
        }
        return schedulerIO;
    }

    /**
     * io线程执行，主线程观察
     * .compose(RxUtils.<T>applySchedulers())
     *
     */
    public static <T> ObservableTransformer<T, T> applySchedulers() {
        return observable -> observable.subscribeOn(getSchedulerIO())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * io线程执行，io线程观察
     * .compose(RxUtils.<T>applySchedulers())
     */
    public static <T> ObservableTransformer<T, T> Schedulers_io() {
        return observable -> observable.subscribeOn(getSchedulerIO())
                .observeOn(getSchedulerIO());
    }

}