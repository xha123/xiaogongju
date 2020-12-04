package com.example.baselib.base

import android.content.Context
import android.os.Bundle
import android.view.View
import com.example.baselib.R
import com.example.baselib.utils.setOnClickListenerWithTrigger
import kotlinx.android.synthetic.main.dialog_hint.*

/**
 * @author: xha
 * @date: 2020/9/22 16:09
 * @Description:提示弹窗
 */
class HintDialog(context: Context) : BaseDialog(context) {
    var mContext: Context? = null

    init {
        this.mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        setContentView(R.layout.dialog_hint)

        tvCancel.setOnClickListenerWithTrigger {
            dismiss()
        }

        tvSure.setOnClickListenerWithTrigger {
            dismiss()
        }

    }


    /**
     * 设置提示内容
     */
    fun setHintText(hintShow: String) {
        tvHint.text = hintShow
    }

    /**
     * 设置按钮文字
     */
    fun setButtonText(cancelShow: String, sureShow: String) {
        tvCancel.text = cancelShow
        tvSure.text = sureShow
    }

    /**
     * 设置是否显示取消按钮
     */
    fun setLeftText(isShow: Boolean) {
        if (isShow) {
            tvCancel.visibility = View.VISIBLE
        } else {
            tvCancel.visibility = View.GONE
        }
    }

    /**
     * 设置左边点击事件
     */
    fun setLeftClickListener(onClickListener: View.OnClickListener) {
        tvCancel.setOnClickListenerWithTrigger(onClickListener)
    }

    /**
     * 设置右边点击事件
     */
    fun setRightClickListener(onClickListener: View.OnClickListener) {
        tvSure.setOnClickListenerWithTrigger(onClickListener)
    }

    /**
     * 设置左键文字和点击事件
     *
     * @param leftStr       文字
     * @param clickListener 点击事件
     */
    fun setLeftButton(leftStr: String, clickListener: View.OnClickListener) {
        tvCancel.setOnClickListenerWithTrigger(clickListener)
        tvCancel.text = leftStr
    }

    /**
     * 设置右键文字和点击事件
     *
     * @param rightStr      文字
     * @param clickListener 点击事件
     */
    fun setRightButton(rightStr: String, clickListener: View.OnClickListener) {
        tvSure.setOnClickListenerWithTrigger(clickListener)
        tvSure.text = rightStr
    }


}