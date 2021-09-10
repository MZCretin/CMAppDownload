package com.roll.codemao.app.data.api;

/**
 * Created by grubber on 2017/1/6.
 */
public class ApiConfig {
    //每次在改线上线下环境的时候记得以下几步
    // 1、修改 HOST_TYPE

    //线上正式
    private static int HOST_TYPE_ONLINE = 0;
    //线下测试
    private static int HOST_TYPE_ONTEST = 1;

    // 记录当前环境 HOST_TYPE_ONLINE线上环境 HOST_TYPE_ONTEST测试环境
    public static int HOST_TYPE = HOST_TYPE_ONTEST;

    //HTTPS
    private static final String BASE_HTTPS = "https://www.mxnzp.com";
    //测试环境
    private static final String BASE_TEST = "http://www.mxnzp.com";

    //线下
    public static final String BASE_URL = (HOST_TYPE == HOST_TYPE_ONTEST)
            ? BASE_TEST : BASE_HTTPS;

}
