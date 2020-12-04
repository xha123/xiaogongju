package com.example.demok.ui.demo

import android.app.Activity
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demok.R
import com.example.demok.base.MyBaseActivity
import com.example.demok.ui.demo.adapter.CusViewAdapter
import kotlinx.android.synthetic.main.layout_recycler_just.*

/**
 * @author: xha
 * @date: 2020/10/22 15:31
 * @Description:自定义view
 */
class CusViewActivity : MyBaseActivity() {

    private val listCus = arrayListOf<String>()
    private val cusViewAdapter by lazy {
        CusViewAdapter()
    }

    companion object {
        @JvmStatic
        fun start(
            context: Activity
        ) {
            val starter = Intent(context, CusViewActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_recycler_just
    }

    override fun initOnCreate() {
        setTitleText("自定义View")

        listCus.add("onDraw")

        setAdapter()
    }

    private fun setAdapter() {
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = cusViewAdapter

        cusViewAdapter.setList(listCus)

        cusViewAdapter.setOnItemClickListener { adapter, view, position ->
            when (position) {
                0 -> {
                    DrawActivity.start(this)
                }
            }
        }
    }

}