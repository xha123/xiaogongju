package com.example.demok.net


import com.example.baselib.utils.LogUtils
import com.google.gson.Gson
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

@Deprecated("弃用")
class HttpManager() {
    private val httpManager: HttpManager? = null
    private val gson: Gson? = null
    private var headerParams: Map<String, String>? = null
    private var map: HashMap<String, Any>? = null

    companion object {

        @Volatile
        var instance: HttpManager? = null

        fun getInstances() =
            instance ?: synchronized(this) {
                instance
                    ?: HttpManager()
                        .also { instance = it }
            }
    }

    /**
     * Get请求
     *
     * @param action   请求接口的尾址
     * @param params   请求参数
     * @param observer observer
     */
    fun get(action: String, params: HashMap<String, Any>?, observer: Observer<ResponseBody>) {
        val getService: ApiService = instance.create(ApiService::class.java)
        getService.getResult(action, headerParams, params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observer);
    }

    /**
     * post请求
     */
    fun post(action: String, params: HashMap<String, Any>?, observer: Observer<ResponseBody>) {
        val getService: ApiService = instance.create(ApiService::class.java)
        getService.postResult(action, params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observer);
    }

    /**
     * 文件上传
     */
    fun post(action: String, fileName: String, observer: Observer<ResponseBody>) {
        val getService: ApiService = instance.create(ApiService::class.java)

        // https://github.com/iPaulPro/aFileChooser/blob/master/aFileChooser/src/com/ipaulpro/afilechooser/utils/FileUtils.java
        // use the FileUtils to get the actual file by uri
        val file: File = File(fileName)

        // create RequestBody instance from file
        val requestFile: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)

        // MultipartBody.Part is used to send also the actual file name
        val body: MultipartBody.Part =
            MultipartBody.Part.createFormData("file", file.name, requestFile)
        getService.upload(action, body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(observer)
    }


    /**
     * 默认接口实现类的实例
     */
    val instance by lazy { createService(ApiService::class.java) }

    /**
     * 生成接口实现类的实例
     */
    private fun <T> createService(apiService: Class<T>): Retrofit {
        return initBaseData()
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
            .baseUrl("")
        return builder1.build()
    }
}