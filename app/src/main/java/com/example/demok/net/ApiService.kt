package com.example.demok.net

import com.example.demok.net.data.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.*
import java.util.*

interface ApiService {


    @GET("")
    fun getResult(
        @Url url: String?,
        @HeaderMap headerParams: Map<String, String>?,
        @QueryMap params: HashMap<String, Any>?
    ): Observable<ResponseBody>


    @FormUrlEncoded
    @POST("")
    fun postResult(
        @Url url: String?,
        @FieldMap params: Map<String, Any>?
    ): Observable<ResponseBody>


    @Multipart
    @POST("")
    fun upload(
        @Url url: String?,
        @Part file: MultipartBody.Part?
    ): Observable<ResponseBody>

    /**
     * 获取用户信息
     */
    @GET("uc/user/detail")
    fun getInfoDetail(
        @Query("userId") userId: String,
        @Query("dataInfo") dataInfo: String
    ): Observable<Result<Any>>

    /**
     * 历史上的今天
     */
    @POST("http://api.juheapi.com/japi/toh")
    fun historyDay(
        @Query("key") key: String,
        @Query("v") v: String,
        @Query("month") month: Int,
        @Query("day") day: Int
    ): Observable<ResultData<List<HistoryData>>>

    /**
     *历史上的今天 详情
     */
    @POST("http://api.juheapi.com/japi/tohdet")
    fun historyDayDetail(
        @Query("key") key: String,
        @Query("v") v: String,
        @Query("id") id: String
    ): Observable<ResultData<List<HistoryData>>>

    /**
     *历史上的今天 详情
     */
    @POST("http://v.juhe.cn/joke/randJoke.php")
    fun smileList(
        @Query("key") key: String
    ): Observable<ResultData<List<SmileData>>>

    /**
     *今日国内油价查询
     */
    @POST("http://apis.juhe.cn/gnyj/query")
    fun oilPriceList(
        @Query("key") key: String
    ): Observable<ResultData<List<OilPriceData>>>

    /**
     *驾照题库
     */
    @POST("http://v.juhe.cn/jztk/query")
    fun qstBankList(
        @Query("key") key: String,
        @Query("subject") subject: String,
        @Query("model") model: String,
        @Query("testType") testType: String
    ): Observable<ResultData<List<QstData>>>

    /**
     *今日国内油价查询
     */
    @POST("http://apis.juhe.cn/gnyj/query")
    suspend fun oilPriceList1(
        @Query("key") key: String
    ): Observable<ResultData<List<OilPriceData>>>
}