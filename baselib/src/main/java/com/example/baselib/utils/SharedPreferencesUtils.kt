package com.example.baselib.utils

import android.content.SharedPreferences
import com.example.baselib.base.BaseApplication

/**
 * @author: xha
 * @date: 2020/9/30 15:07
 * @Description:
 */
object SharedPreferencesUtils {
    /**
     * 新增方法
     */
    private val SHARED_NAME = "share_data"

    fun putShared(key: String?, value: Any?) {
        val preference: SharedPreferences =
            BaseApplication.mContext.getSharedPreferences(SHARED_NAME, 0)
        val editors: SharedPreferences.Editor = preference.edit()
        when (value) {
            is String -> {
                editors.putString(key, value as String?)
            }
            is Int -> {
                editors.putInt(key, (value as Int?)!!)
            }
            is Float -> {
                editors.putFloat(key, (value as Float?)!!)
            }
            is Boolean -> {
                editors.putBoolean(key, (value as Boolean?)!!)
            }
            is Long -> {
                editors.putLong(key, (value as Long?)!!)
            }
        }
        editors.apply()
    }


     fun getShared(key: String?, defaultValue: Any?): Any? {
        val preference: SharedPreferences =
            BaseApplication.mContext.getSharedPreferences(SHARED_NAME, 0)
        return when (defaultValue) {
            is String -> {
                preference.getString(key, defaultValue as String?)
            }
            is Int -> {
                preference.getInt(key, (defaultValue as Int?)!!)
            }
            is Float -> {
                preference.getFloat(key, (defaultValue as Float?)!!)
            }
            is Boolean -> {
                preference.getBoolean(key, (defaultValue as Boolean?)!!)
            }
            is Long -> {
                preference.getLong(key, (defaultValue as Long?)!!)
            }
            else -> {
                //            return preference.get(key, (Object) defaultValue);
                throw RuntimeException("错误类型")
            }
        }
    }

    fun clear() {
        val preference: SharedPreferences =
            BaseApplication.mContext.getSharedPreferences(SHARED_NAME, 0)
        val editors: SharedPreferences.Editor = preference.edit()
        editors.clear().apply()
    }
}