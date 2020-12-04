package com.example.demok.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.example.baselib.utils.setOnClickListenerWithTrigger
import com.example.demok.R
import com.example.demok.base.MyApplication
import com.example.demok.base.MyBaseActivity
import com.example.demok.net.DataManager
import com.example.demok.net.MyObserver
import com.example.demok.net.ResponseCallBack
import com.example.demok.net.data.HistoryData
import com.example.demok.net.data.ResultData
import com.example.demok.utils.Const
import com.example.demok.utils.ImageUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_historydetail.*

/**
 * @author: xha
 * @date: 2020/10/9 14:45
 * @Description:
 */
class HisDetailActivity : MyBaseActivity() {

    private var hisData: HistoryData? = null

    companion object {
        @JvmStatic
        fun start(context: Activity, hisData: HistoryData) {
            val starter = Intent(context, HisDetailActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable(Const.Intent.DATA, hisData)
            starter.putExtras(bundle)
            context.startActivity(starter)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_historydetail
    }

    override fun initOnCreate() {
        hisData = intent.extras!!.getParcelable(Const.Intent.DATA)

        showTitle(false)
        showHeader(false)

        hisData.let {
            if (it?.content.isNullOrEmpty() || it?.content == "null") {
                getData()
            } else {
                setShow(it!!)
            }
        }
    }

    override fun initOnClickerListener() {
        super.initOnClickerListener()
        ivBackHisDetail.setOnClickListenerWithTrigger { onBackPressed() }
        iv_head_hisdetail.setOnClickListenerWithTrigger {
            val imgList = arrayListOf<String>()
            imgList.add(hisData!!.pic)
            PhotoActivity.start(this, imgList)
        }
    }

    private fun getData() {
        showProDialog()
        DataManager.getInstances().apiService.historyDayDetail(Const.JUHE_KEY, "1.0", hisData?.id!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(MyObserver(object : ResponseCallBack<ResultData<List<HistoryData>>> {
                override fun onSuccess(t: ResultData<List<HistoryData>>) {
                    dismissProDialog()
                    if (t.result.isNotEmpty()) {
                        setShow(t.result[0])
                    }
                }

                override fun onFail(code: String, errorMsg: String) {
                    dismissProDialog()
                }

            }))
    }

    private fun setShow(body: HistoryData) {
        ImageUtils.loadUri(iv_head_hisdetail, body.pic)
        tvTitleHisDetail.text = body.title
        tvShowHisDetial.text = body.content

        //更新
        MyApplication.mContext.hisList.forEach {
            if (it.id == body.id) {
                it.pic = body.pic
                it.content = body.content
            }
        }
    }

}