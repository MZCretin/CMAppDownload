package com.roll.codemao.app.data.api.service

import com.roll.codemao.app.data.api.ApiConfig
import com.roll.codemao.app.data.api.model.Result
import com.roll.codemao.model.resp.*
import io.reactivex.Observable
import retrofit2.http.*

/**
 * Created by cretin on 2018/3/29.
 * 与用户相关的操作
 */
interface ApiService {
    /**
     * 测试数据
     *
     * @return
     */
    @GET("http://www.mxnzp.com/api/holiday/list/year/2009")
    fun testData(): Observable<Result<MutableList<TestModel>>>

    /**
     * 测试数据
     *
     * @return
     */
    @GET("http://www.mxnzp.com/api/ip/self")
    fun testData1(): Observable<Result<Test1Model>> //

    /**
     * 测试数据
     *
     * @return
     */
    @GET("http://www.mxnzp.com/api/ip/self")
    fun testData2(): Observable<Test2Model>

    /**
     * 获取app列表
     *
     * @return
     */
    @POST("https://www.pgyer.com/apiv2/app/listMy")
    @FormUrlEncoded
    fun getHomeAppList(@Field("_api_key") _api_key: String,
                       @Field("page") page: Int): Observable<Result<HomeAppListRootResp>>

    /**
     * 获取app版本列表
     *
     * @return
     */
    @POST("https://www.pgyer.com/apiv2/app/builds")
    @FormUrlEncoded
    fun getHomeAppVersionList(@FieldMap request: Map<String, String>): Observable<Result<HomeAppListRootResp>>

}