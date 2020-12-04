package com.example.demok.base

import android.annotation.TargetApi
import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Process
import androidx.multidex.MultiDex
import cn.jpush.android.api.JPushInterface
import com.example.baselib.BuildConfig
import com.example.baselib.base.BaseApplication
import com.example.demok.net.data.HistoryData
import com.example.demok.utils.Const
import com.facebook.drawee.backends.pipeline.Fresco
import com.tencent.bugly.Bugly
import dagger.hilt.android.HiltAndroidApp

/**
 * @author: xha
 * @date: 2020/9/28 14:40
 * @Description:
 */
@HiltAndroidApp
class MyApplication : BaseApplication() {

    var hisList: List<HistoryData> = arrayListOf()

    companion object {
        lateinit var mContext: MyApplication
        fun init(myApplication: MyApplication) {
            this.mContext = myApplication
        }
    }

    override fun onCreate() {
        super.onCreate()
        init(this)

        if (getCurProcessName() == applicationInfo.packageName) {
            setFresco()
            setBugly()
            setJPush()
        }
    }

    private fun setJPush() {
        JPushInterface.setDebugMode(BuildConfig.DEBUG)
        JPushInterface.init(this)

    }

    private fun setBugly() {
        Bugly.init(applicationContext, Const.BUGLY_APPID, BuildConfig.DEBUG);
    }


    private fun setFresco() {
        Fresco.initialize(this);
//        val config: ImagePipelineConfig = ImageLoaderConfig.getImagePipelineConfig(this)
//        Fresco.initialize(this, config)
    }


    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    fun getCurProcessName(): String? {
        val pid = Process.myPid()
        val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val list = activityManager.runningAppProcesses
        if (list != null) {
            for (runningAppProcessInfo in list) {
                if (runningAppProcessInfo.pid == pid) {
                    return runningAppProcessInfo.processName
                }
            }
        }
        return ""
    }


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)


    }
}