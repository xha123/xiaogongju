package com.example.demok.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

/**
 * @author: xha
 * @date: 2020/9/30 11:08
 * @Description:
 */
object MyUtils {

    /**
     * 根据时间戳显示时间
     */
    fun showTimeYMD(timestamp: Long): String {
        val simpleDateFormat =
            SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
        return simpleDateFormat.format(Date(timestamp))
    }

    //根据时间戳显示时间
    fun showTime(timestamp: Long, pattern: String?): String {
        val simpleDateFormat =
            SimpleDateFormat(pattern, Locale.CHINA)
        return simpleDateFormat.format(Date(timestamp))
    }

    /**
     * 判断是否为非零开头的最多带两位小数的数字
     */
    fun isNumberFor2(str: String): Boolean {
        val regex = "^(0|([1-9][0-9]?))(.|.[0-9]{1,2})?\$"
        return Pattern.matches(regex, str)
    }

    /**
     * 判断是否为整数
     */
    fun isNumber(str: String): Boolean {
        val regex = "^([0-9]|[1-9][0-9]?)\$"
        return Pattern.matches(regex, str)
    }

    /**
     * 将“0-9”转换为“00-09”
     */
    fun formatTime(unit: Int): String? {
        return if (unit < 10) "0$unit" else unit.toString()
    }

    fun formatTime(unit: Long): String? {
        return if (unit < 10) "0$unit" else unit.toString()
    }

    /**
     * 跳转到应用详情界面
     */
    fun gotoAppDetailIntent(activity: Activity, resquestCode: Int) {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = Uri.parse("package:" + activity.packageName)
        activity.startActivityForResult(intent, resquestCode)
    }

    /**
     * 复制到剪切板
     *
     * @param copyStr
     * @return
     */
    fun copyStr(context: Context, copyStr: String?) {
        //获取剪贴板管理器
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        // 创建普通字符型ClipData
        val mClipData = ClipData.newPlainText("Label", copyStr)
        // 将ClipData内容放到系统剪贴板里。
        cm.setPrimaryClip(mClipData)
    }

    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale: Float = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }
}