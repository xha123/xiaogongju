package com.example.baselib.base

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.example.baselib.R
import com.example.baselib.utils.setOnClickListenerWithTrigger
import kotlinx.android.synthetic.main.dialog_pro.*

/**
 * @author: xha
 * @date: 2020/9/22 16:09
 * @Description:提示弹窗
 */
class ProgressDialog(context: Context) : BaseDialog(context) {
    var mContext: Context? = null
    var isCancelAble: Boolean = true//是否可以取消
    var isonTouchOutSide: Boolean = false

    init {
        this.mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //处理物理返回键是否可以取消弹窗
        setCancelable(true)
        //处理点击弹窗外部是否可以取消弹窗
        setCanceledOnTouchOutside(true)
        setContentView(R.layout.dialog_pro)
        window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window!!.setGravity(Gravity.CENTER)

    }


    /**
     * 设置提示内容
     */
    fun setProText(hintShow: String) {
        message.text = hintShow
    }



}