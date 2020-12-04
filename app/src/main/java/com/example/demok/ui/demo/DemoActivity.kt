package com.example.demok.ui.demo

import android.app.Activity
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baselib.utils.showToast
import com.example.demok.R
import com.example.demok.base.MyBaseActivity
import com.example.demok.ui.demo.adapter.DemoAdapter
import com.example.demok.ui.demo.fish.FishActivity
import kotlinx.android.synthetic.main.layout_recycler_just.*

/**
 * @author: xha
 * @date: 2020/10/22 15:16
 * @Description:小功能
 */
class DemoActivity : MyBaseActivity() {

    val demoList = arrayListOf<String>()
    private val demoAdapter by lazy {
        DemoAdapter()
    }

    companion object {
        @JvmStatic
        fun start(
            context: Activity
        ) {
            val starter = Intent(context, DemoActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.layout_recycler_just
    }

    override fun initOnCreate() {
        setTitleText("小demo")

        demoList.add("鲤鱼动画")
        demoList.add("横屏滚动")
        demoList.add("自定义动画")
        demoList.add("hilt")
        demoList.add("获取手机信息")
        demoList.add("databinding")

        setAdapter()
    }

    private fun setAdapter() {
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = demoAdapter

        demoAdapter.setList(demoList)

        demoAdapter.setOnItemClickListener { _, _, position ->
            when (position) {
                0 -> {
                    FishActivity.start(this)
                }
                1 -> {
                    RollActivity.start(this)
                }
                2 -> {
                    CusViewActivity.start(this)
                }
                3 -> {
                    HiltDemoActivity.start(this)
                }
                4 -> {
                    PhoneGetActivity.start(this)
                }
                5 -> {
                    DataBindActivity.start(this)
                }
            }
        }
    }
}