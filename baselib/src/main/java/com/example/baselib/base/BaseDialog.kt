package com.example.baselib.base

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import com.example.baselib.R

/**
 * @author kyp
 * @date 2020/7/24
 * @desc dialog父类
 */
open class BaseDialog (context: Context) : Dialog(context, R.style.common_dialog){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCanceledOnTouchOutside(false)
    }

    /**
     * 设置dialog宽度全屏
     */
    fun setFullScreenWidth() {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val window = window ?: return
        val lp = window.attributes
        val outSize = Point()
        display.getSize(outSize)
        lp.width = outSize.x
        window.attributes = lp
    }

    /**
     * 设置dialog位于屏幕底部
     */
    fun setAtBottom() {
        val window = window
        window?.setGravity(Gravity.BOTTOM)
    }

    fun setScreenBgLight() {
        val window = window
        if (window != null) {
            val lp = window.attributes
            lp.alpha = 1.0f
            lp.dimAmount = 0.0f
            window.attributes = lp
        }
    }
}