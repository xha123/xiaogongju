package com.example.demok.net

/**
 * 请求回调
 */
interface ResponseCallBack<T> {
    fun onSuccess(t: T)

    fun onFail(code:String,errorMsg: String)
}