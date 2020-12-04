package com.example.demok.ui.demo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.baselib.utils.LogUtils
import com.example.demok.R
import kotlinx.android.synthetic.main.activity_roll.*
import kotlinx.android.synthetic.main.item_roll.view.*

/**
 * @author: xha
 * @date: 2020/10/12 9:24
 * @Description:
 */
class RollActivity : AppCompatActivity() {

    private val strList: MutableList<String> = arrayListOf()

    private val rollAdapter by lazy {
        RollAdapter()
    }


    companion object {
        @JvmStatic
        fun start(
            context: Activity
        ) {
            val starter = Intent(context, RollActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_roll)

        strList.add("收到了看法就阿里ASLD")
        strList.add("收到了")
        strList.add("收到了sdfalkj收到了看法就 ")
        strList.add("收到了sdfalkj收到了看法就大立科技啊  ")
        strList.add("收到了sdfalkj收到了看法就大是哒  ")
        setAdapter()

    }

//    override fun getLayoutId(): Int {
//        return R.layout.activity_roll
//    }
//
//    override fun initOnCreate() {
//
//    }

    /**
     * 配合fragment使用时可以通过setUserVisibleHint()判断调用开始和结束动画，
     * 我这里封装了手动滑动的情况，如果不需要手动滑动，请禁用recycler滑动事件
     */
    private fun setAdapter() {
        val gridLayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)
        mRecyclerView_Roll.layoutManager = gridLayoutManager
        mRecyclerView_Roll.adapter = rollAdapter


        rollAdapter.setList(strList)

        mRecyclerView_Roll.startMove()

        rollAdapter.loadMoreModule.setOnLoadMoreListener {
            LogUtils.i("加载更多...")
            rollAdapter.addData(strList)
            rollAdapter.loadMoreModule.loadMoreComplete()
        }

    }

    class RollAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_roll),LoadMoreModule {


        override fun convert(holder: BaseViewHolder, item: String) {
            holder.itemView.tvShowRoll.text = item
        }

    }
}