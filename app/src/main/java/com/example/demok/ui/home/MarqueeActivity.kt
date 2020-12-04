package com.example.demok.ui.home

import android.app.Activity
import android.content.Intent
import com.example.baselib.utils.setOnClickListenerWithTrigger
import com.example.baselib.utils.showToast
import com.example.demok.R
import com.example.demok.base.MyBaseActivity
import kotlinx.android.synthetic.main.activity_marquee.*

/**
 * @author: xha
 * @date: 2020/10/15 17:28
 * @Description:
 */
class MarqueeActivity : MyBaseActivity() {

    companion object {
        @JvmStatic
        fun start(
            context: Activity
        ) {
            val starter = Intent(context, MarqueeActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_marquee
    }

    override fun initOnCreate() {
        setTitleText("跑马灯文字")

        tvChooseMarquee.isSelected = true
    }

    override fun initOnClickerListener() {
        super.initOnClickerListener()
        tvSureMarquee.setOnClickListenerWithTrigger {
            val str = edtMarquee.text.toString()
            if (str.isNullOrEmpty()) {
                showToast("请输入文本内容")
                return@setOnClickListenerWithTrigger
            }

            MarqueeShowActivity.start(this, str, tvChooseMarquee.isSelected)
        }

        tvChooseMarquee.setOnClickListenerWithTrigger {
            tvChooseMarquee.isSelected = !tvChooseMarquee.isSelected
        }
    }

}