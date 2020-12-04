package com.example.baselib.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.TelephonyManager
import java.util.*


/**
 * @author wq
 * @date 2019/4/25 下午1:50
 * @desc ${TODO}
 */
object SystemUtils {


    /**
     * 获取当前手机系统版本号
     *
     * @return  系统版本号
     */
    fun getSystemVersion(): String {
        return Build.VERSION.RELEASE
    }

    /**
     * 获取手机型号
     *
     * @return  手机型号
     */
    fun getSystemModel(): String {
        return Build.MODEL
    }

    /**
     * 获取手机厂商
     *
     * @return  手机厂商
     */
    fun getDeviceBrand(): String {
        return Build.BRAND
    }

    fun getRom(): ROM {

        var manufacturer = Build.MANUFACTURER

        when (manufacturer) {

            "Xiaomi" -> {
                return ROM.MIUI
            }
            "HUAWEI" -> {
                return ROM.EMUI
            }

        }

        return ROM.Other

    }



    /**
     * 获取当前应用版本名称
     */
    fun getAppVersionName(context: Context): String {

        val pi = context.packageManager.getPackageInfo(context.packageName, 0)
        return pi.versionName
    }

    /**
     * 获取当前应用版本号
     */
    fun getAppVersionCode(context: Context): Long {

        val pi = context.packageManager.getPackageInfo(context.packageName, 0)

        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            pi.longVersionCode
        } else {
            pi.versionCode.toLong()
        }

    }

    /**
     * 判断当前手机是否为小米手机
     */
    fun isBrandXiaoMi(): Boolean {

        return "xiaomi".equals(
            Build.BRAND,
            ignoreCase = true
        ) || "xiaomi".equals(Build.MANUFACTURER, ignoreCase = true)
    }

    /**
     * 判断当前手机是否为华为手机
     */
    fun isBrandHuawei(): Boolean {

        return "huawei".equals(
            Build.BRAND,
            ignoreCase = true
        ) || "huawei".equals(Build.MANUFACTURER, ignoreCase = true)
    }


}