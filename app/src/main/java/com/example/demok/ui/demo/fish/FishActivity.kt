package com.example.demok.ui.demo.fish

import android.app.Activity
import android.content.Intent
import com.example.demok.R
import com.example.demok.base.MyBaseActivity

/**
 * @author: xha
 * @date: 2020/10/19 14:58
 * @Description:
 */
class FishActivity : MyBaseActivity() {

    companion object {
        @JvmStatic
        fun start(
            context: Activity
        ) {
            val starter = Intent(context, FishActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_fish
    }

    override fun initOnCreate() {
        setTitleText("鲤鱼动画")
    }

}