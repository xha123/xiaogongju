package com.example.demok.ui.home

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demok.R
import com.example.demok.base.MyApplication
import com.example.demok.base.MyBaseActivity
import com.example.demok.net.DataManager
import com.example.demok.net.MyObserver
import com.example.demok.net.ResponseCallBack
import com.example.demok.net.data.HistoryData
import com.example.demok.net.data.ResultData
import com.example.demok.ui.adapter.HistoryListAdapter
import com.example.demok.utils.Const
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.layout_recycler_just.*
import java.util.*


/**
 * @author: xha
 * @date: 2020/9/30 17:01
 * @Description:历史上的今天
 */
class HistoryListActivity : MyBaseActivity() {

    private val historyAdapter by lazy {
        HistoryListAdapter()
    }

    companion object {
        @JvmStatic
        fun start(
            context: Activity
        ) {
            val starter = Intent(context, HistoryListActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_recycler_just
    }

    override fun initOnCreate() {
        setTitleText("历史上的今天")
        setAdapter()
        val hisList = MyApplication.mContext.hisList
        if (hisList.isNotEmpty()) {
            historyAdapter.setList(hisList)
        } else {
            getData()
        }

    }

    private fun getData() {
//        val httpPost = "http://api.juheapi.com/japi/toh"
//        val calendar = Calendar.getInstance()
//        val map = HashMap<String, Any>()
//        map["key"] = "ad40ad6bc70f321b10bdd2a5c66d27ee"
//        map["v"] = "1.0"
//        map["month"] = calendar.get(Calendar.MONTH) + 1
//        map["day"] = calendar.get(Calendar.DAY_OF_MONTH)

        showProDialog()
        val calendar = Calendar.getInstance()
        DataManager.getInstances().apiService.historyDay(
            Const.JUHE_KEY,
            "1.0", calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(MyObserver(object : ResponseCallBack<ResultData<List<HistoryData>>> {
                override fun onSuccess(t: ResultData<List<HistoryData>>) {
                    dismissProDialog()
                    if (t.result.isNotEmpty()) {
                        historyAdapter.setList(t.result)
                        MyApplication.mContext.hisList = t.result
                    }
                }

                override fun onFail(code: String, errorMsg: String) {
                    dismissProDialog()
                }

            }))

    }

    private fun setAdapter() {
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = historyAdapter


        val viewEmpty = View.inflate(this, R.layout.layout_nodata, null)
        historyAdapter.setEmptyView(viewEmpty)

        historyAdapter.setOnItemClickListener { adapter, view, position ->
            val hisData = adapter.getItem(position) as HistoryData
            HisDetailActivity.start(this, hisData)
        }

    }
}