package com.example.demok.ui.demo

import android.app.Activity
import android.content.Intent
import com.example.demok.R
import com.example.demok.base.MyBaseActivity
import com.example.demok.ui.demo.model.Driver
import com.example.demok.ui.demo.model.Truck
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * @author: xha
 * @date: 2020/11/6 10:02
 * @Description:
 */
@AndroidEntryPoint
class HiltDemoActivity : MyBaseActivity() {

    @Inject
    lateinit var truck: Truck

    @Inject
    lateinit var driver: Driver

    companion object {
        @JvmStatic
        fun start(
            context: Activity
        ) {
            val starter = Intent(context, HiltDemoActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_hilt_demo
    }

    override fun initOnCreate() {
        truck.driver.name = "大卡车"
        driver.year = "开了3年了"
        truck.deliver()

    }

}