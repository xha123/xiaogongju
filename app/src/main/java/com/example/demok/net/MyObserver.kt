package com.example.demok.net


import com.example.baselib.utils.LogUtils
import com.example.baselib.utils.showToast
import com.example.demok.R
import com.example.demok.base.MyApplication
import com.example.demok.net.data.ResultData
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

/**
 * 统一处理请求结果的BaseObserver
 */
open class MyObserver<T>(
    private var responseCallBack: ResponseCallBack<T>,
    private var isShow: Boolean = true
) : Observer<T> {
    private var disposable: Disposable? = null
//    private var mPopupWindow: PopupWindow? = null

    /**
     * 进行请求开始时的初始化操作
     */
    override fun onSubscribe(d: Disposable) {
        this.disposable = d
    }

    /**
     * 处理请求成功业务
     */
    override fun onNext(t: T) {
        val result = t as ResultData<*>
        LogUtils.i("onNext>>$result")
        if (result.code.isNullOrEmpty()) {
            when (result.error_code) {
                0 -> {
                    responseCallBack.onSuccess(t)
                }
                else -> {
                    if (result.error_code == 0) {
                        responseCallBack.onSuccess(t)
                    } else {
                        errorCheck(result.code.toString(), result.message!!)
                    }
                }
            }
        } else {
            when (result.code) {
                "0", "0000" -> {
                    responseCallBack.onSuccess(t)
                }
                else -> {
                    if (result.error_code == 0) {
                        responseCallBack.onSuccess(t)
                    } else {
                        errorCheck(result.code, result.message!!)
                    }
                }
            }
        }
    }

    private fun errorCheck(code: String, message: String) {
        if (isShow) {
//            Toast.makeText(App.mContext, message, Toast.LENGTH_SHORT).show()
            MyApplication.mContext.showToast(message)
        }
        responseCallBack.onFail(code, message)
    }

    /**
     * 统一处理请求失败信息
     */
    override fun onError(e: Throwable) {
        var errorStr = ""
        try {
            when (e) {
                is SocketTimeoutException -> //请求超时
                    errorStr = MyApplication.mContext.resources.getString(R.string.http_timeout)
                is ConnectException -> //网络连接超时
                    errorStr = MyApplication.mContext.resources.getString(R.string.http_connectout)
                is SSLHandshakeException -> //安全证书异常
                    errorStr =
                        MyApplication.mContext.resources.getString(R.string.http_sslhandshake)
                is HttpException -> //请求的地址不存在
                    errorStr = when (e.code()) {
                        504 -> MyApplication.mContext.resources.getString(R.string.http_netwotkerrot)
                        404 -> MyApplication.mContext.resources.getString(R.string.http_notfound)
                        403 -> MyApplication.mContext.resources.getString(R.string.http_neterror)
                        else -> {
                            MyApplication.mContext.resources.getString(R.string.http_reqesterror)
                        }
                    }

                is UnknownHostException -> //域名解析失败
                    errorStr =
                        MyApplication.mContext.resources.getString(R.string.http_netwotkerrot)
                is IllegalStateException -> {
                    errorStr = MyApplication.mContext.resources.getString(R.string.http_changeerror)
                }
                else -> {
                    errorStr = "error:" + e.message
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            LogUtils.e("onnext>>$errorStr")
            errorCheck("1", errorStr)

            if (!disposable?.isDisposed!!) {
                disposable?.dispose()
            }
        }
    }

    /**
     * 处理请求成功结束后的业务
     */
    override fun onComplete() {

        if (!disposable?.isDisposed!!) {
            disposable?.dispose()
        }

    }
}