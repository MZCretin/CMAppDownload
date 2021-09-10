package com.roll.codemao.base.di

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by LiuG on 2019-12-12.
 *
 * 用于同步cookie
 */
class CookieInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
//        builder.addHeader("X-CodeMao-Mobile-Requested", DeviceInformationUtil.getHeader())
//        if (!TextUtils.isEmpty(CodeMaoAccount.getAccountToken())) {
//            builder.addHeader("authorization", CodeMaoAccount.getAccountToken())
//            //builder.addHeader("cookie", env + CodeMaoAccount.getAccountToken()).build();
//        }
        val request = builder.build()
        return chain.proceed(request)
    }
}