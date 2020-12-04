package com.example.baselib.base

import android.app.Application

/**
 * @author: xha
 * @date: 2020/9/28 14:34
 * @Description:
 */
abstract class BaseApplication : Application() {

    companion object {
        lateinit var mContext: Application
        fun init(application: Application) {
            this.mContext = application
        }
    }

    override fun onCreate() {
        super.onCreate()
        init(this)

    }
}