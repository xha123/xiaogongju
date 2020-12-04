package com.example.demok.ui.demo

import android.app.Activity
import android.content.Intent
import com.example.baselib.utils.setOnClickListenerWithTrigger
import com.example.demok.R
import com.example.demok.base.MyBaseActivity
import kotlinx.android.synthetic.main.activity_draw.*

/**
 * @author: xha
 * @date: 2020/10/22 15:39
 * @Description:
 */
class DrawActivity : MyBaseActivity() {

    companion object {
        @JvmStatic
        fun start(
            context: Activity
        ) {
            val starter = Intent(context, DrawActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_draw
    }

    override fun initOnCreate() {


    }

    override fun initOnClickerListener() {
        super.initOnClickerListener()
        btnDraw.setOnClickListenerWithTrigger {
            tvScanDraw.startAnim()
        }
    }



}