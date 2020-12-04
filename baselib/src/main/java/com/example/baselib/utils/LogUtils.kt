package com.example.baselib.utils

import android.util.Log
import com.example.baselib.BuildConfig


/**
 * @author wq
 * @date 2019-04-28 17:24
 * @desc ${TODO}
 */
object LogUtils {

    private var tag = "tag"

    fun d(msg: String) {
        if (BuildConfig.DEBUG) {
            i("", msg)
        }
    }

    fun i(msg: String) {
        if (BuildConfig.DEBUG) {
            i("", msg)
        }
    }

    fun i(tagShow: String?, msg: String) {
        if (!BuildConfig.DEBUG) {
            return
        }
        val segmentSize = 3 * 1024
        var msgShow = msg
        if (msgShow.length > segmentSize) {
            while (msgShow.length > segmentSize) { // 循环分段打印日志
                val logContent = msgShow.substring(0, segmentSize)
                Log.i("$tag.$tagShow", logContent)
                msgShow = msgShow.replace(logContent, "")
            }
            Log.i("$tag.$tagShow", msgShow)
        } else {
            Log.i("$tag.$tagShow", msg)
        }
    }


    fun e(msg: String) {
        if (BuildConfig.DEBUG) {
            e("", msg)
        }
    }

    fun e(tagShow: String?, msg: String) {
        if (!BuildConfig.DEBUG) {
            return
        }
        val segmentSize = 3 * 1024
        var msgShow = msg
        if (msgShow.length > segmentSize) {
            while (msgShow.length > segmentSize) { // 循环分段打印日志
                val logContent = msgShow.substring(0, segmentSize)
                msgShow = msgShow.replace(logContent, "")
                Log.e("$tag.$tagShow", logContent)
            }
            Log.e("$tag.$tagShow", msgShow)
        } else {
            Log.e("$tag.$tagShow", msg)
        }
    }
}