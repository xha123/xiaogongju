package com.example.baselib.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.example.baselib.base.BaseApplication

/**
 * @author: xha
 * @date: 2020/9/9 11:55
 * @Description:广播管理类
 */
class BroadcastManager(mContext: Context) {

    private var mContext: Context? = null
    private val receiverMap = HashMap<String, BroadcastReceiver>()

    init {
        this.mContext = mContext
    }

    companion object {
        @Volatile
        var broadcastManager: BroadcastManager? = null

        fun getInstance() =
            broadcastManager ?: synchronized(this) {
                broadcastManager ?: BroadcastManager(BaseApplication.mContext).also { broadcastManager = it }
            }
    }

    /**
     * 添加action
     */
    fun addAction(action: String, receiver: BroadcastReceiver) {
        try {
            val filter = IntentFilter()
            filter.addAction(action)
            mContext?.registerReceiver(receiver, filter)
            receiverMap.put(action, receiver)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 发送广播
     * @param 广播Action
     */
    fun sendBroadCast(action: String?) {
        sendBroadcast(action, "")
    }

    /**
     * 发送String类型广播
     */
    fun sendBroadcast(action: String?, s: String?) {
        val intent = Intent()
        intent.action = action
        intent.putExtra(Constants.DATA, s)
        mContext!!.sendBroadcast(intent)
    }

    fun sendBroadcast(action: String?, s: Int) {
        val intent = Intent()
        intent.action = action
        intent.putExtra(Constants.DATA, s)
        mContext!!.sendBroadcast(intent)
    }

    fun sendBroadcast(action: String?, enabled: Boolean) {
        val intent = Intent()
        intent.action = action
        intent.putExtra(Constants.DATA, enabled)
        mContext!!.sendBroadcast(intent)
    }

    /**
     * 销毁广播
     */
    fun destroy(action: String?) {
        val receiver = receiverMap.remove(action)
        if (receiver != null) {
            mContext!!.unregisterReceiver(receiver)
        }
    }


}