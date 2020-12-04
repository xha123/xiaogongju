package com.example.demok.control.jpush

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import cn.jpush.android.api.CustomMessage
import cn.jpush.android.api.NotificationMessage
import cn.jpush.android.service.JPushMessageReceiver
import com.example.baselib.utils.LogUtils
import com.example.demok.R
import org.json.JSONObject
import java.lang.Exception

/**
 * @author: xha
 * @date: 2020/12/4 10:02
 * @Description:
 */
class MyJPushMessageReceiver : JPushMessageReceiver() {

    //收到通知回调
    override fun onNotifyMessageArrived(p0: Context?, p1: NotificationMessage?) {
        super.onNotifyMessageArrived(p0, p1)
        LogUtils.i("收到通知回调")
        LogUtils.i("通知内容 " + p1.toString())
        val extJson = JSONObject(p1?.notificationExtras.toString())
        when (extJson.getString("isVoice")) {
            "1" -> {
                try {
                    val mediaPlayer = MediaPlayer.create(p0, R.raw.voice)
                    mediaPlayer.start()
                } catch (e: Exception) {
                    LogUtils.i("通知音频播放失败")
                    e.printStackTrace()
                }

            }
        }
    }

    override fun onNotifyMessageOpened(p0: Context?, p1: NotificationMessage?) {
        super.onNotifyMessageOpened(p0, p1)
        LogUtils.i("点击通知回调")
    }

    override fun onNotificationSettingsCheck(p0: Context?, p1: Boolean, p2: Int) {
        super.onNotificationSettingsCheck(p0, p1, p2)
        LogUtils.i("通知开关的回调")
    }


}