package com.example.demok.net

import android.content.Context
import com.example.baselib.utils.LogUtils
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

class DataManager() {
    private var headerParams: Map<String, String>? = null
    private var map: HashMap<String, Any>? = null
    private var baseUrl: String = "http://api.juheapi.com/"

    companion object {

        @Volatile
        var instance: DataManager? = null

        fun getInstances() =
            instance ?: synchronized(this) {
                instance ?: DataManager().also { instance = it }
            }
    }

    /**
     * by lazy 懒加载（延迟加载）
     */
    private val mRetrofit by lazy {
        initBaseData()
    }

    /**
     * 默认接口实现类的实例
     */
    val apiService by lazy { createService(ApiService::class.java) }

    /**
     * 生成接口实现类的实例
     */
    private fun <T> createService(apiService: Class<T>): T {
        return mRetrofit.create(apiService)
    }

    /**
     * 初始化请求头，具体情况根据需求设置
     */
    fun initHeader() {
        headerParams = HashMap<String, String>()
        map = HashMap<String, Any>()
        // 传参方式为json
        (headerParams as HashMap<String, String>).put("Content-Type", "application/json")
    }

    /**
     * 初始化数据
     *
     * @param action 当前请求的尾址
     */
    private fun initBaseData(): Retrofit {
        initHeader()
        // 监听请求条件
        val builder = OkHttpClient.Builder()
        builder.connectTimeout(20, TimeUnit.SECONDS)
        builder.addInterceptor(object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request()

                LogUtils.i("zzz", "request====2" + request.headers.toString())
                LogUtils.e("zzz接口信息", "request====3$request")
                val proceed = chain.proceed(request)
                LogUtils.i("zzz", "proceed====4" + proceed.headers.toString())
                return proceed
            }
        })
        val builder1 = Retrofit.Builder()
            .client(builder.build()) // 配置监听请求
            .addConverterFactory(GsonConverterFactory.create()) // 请求结果转换（当前为GSON）
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // 请求接受工具（当前为RxJava2）
        //        builder1.baseUrl(BASE_URL + action.substring(0, action.lastIndexOf("/") + 1));
        builder1.baseUrl(baseUrl)
        return builder1.build()
    }
}