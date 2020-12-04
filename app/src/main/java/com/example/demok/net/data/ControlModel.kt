package com.example.demok.net.data

import com.example.baselib.utils.LogUtils
import com.example.demok.base.MyBaseActivity
import javax.inject.Inject

/**
 * @author: xha
 * @date: 2020/10/22 15:21
 * @Description:
 */

/**
 * 小demo
 */
data class DemoModel(var strShow: String, var clazz: Class<MyBaseActivity>)

data class PriceModel(var title: String) {
    var price: String = ""
    var unit: String = ""
    var unitShow: Double = 0.0
    var endShow:String = "我便宜"
    fun getResult(): Double {
        try {
            unitShow = price.toDouble() / unit.toDouble()
        } catch (e: Exception) {
            LogUtils.i("转换出错")
            e.printStackTrace()
        } finally {
            return unitShow
        }
    }
}