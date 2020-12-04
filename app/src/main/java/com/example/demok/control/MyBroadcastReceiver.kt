package com.example.demok.control

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

/**
 * @author: xha
 * @date: 2020/11/26 15:33
 * @Description:
 */
class MyBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "接收到关闭或打开飞行模式广播", Toast.LENGTH_LONG).show();
    }

}