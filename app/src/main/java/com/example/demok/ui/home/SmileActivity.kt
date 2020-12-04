package com.example.demok.ui.home

import android.app.Activity
import android.content.Intent
import android.view.View
import com.example.baselib.utils.setOnClickListenerWithTrigger
import com.example.baselib.utils.showToast
import com.example.demok.R
import com.example.demok.base.MyBaseActivity
import com.example.demok.net.DataManager
import com.example.demok.net.MyObserver
import com.example.demok.net.ResponseCallBack
import com.example.demok.net.data.ResultData
import com.example.demok.net.data.SmileData
import com.example.demok.utils.Const.JUHE_SMILE_KEY
import com.example.demok.utils.MyUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_smile.*

/**
 * @author: xha
 * @date: 2020/10/9 16:43
 * @Description: 开心一下
 */
class SmileActivity : MyBaseActivity() {

    var smileList: MutableList<SmileData> = arrayListOf()
    private var postion: Int = -1

    companion object {
        @JvmStatic
        fun start(
            context: Activity
        ) {
            val starter = Intent(context, SmileActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_smile
    }

    override fun initOnCreate() {
        setTitleText("开心一下")
        getData()
    }

    override fun initOnClickerListener() {
        super.initOnClickerListener()
        tvLastSmile.setOnClickListenerWithTrigger {
            if (postion > 0) {
                postion--
                setShow()
            } else {
                showToast("已经是第一条了")
            }

        }

        tvNextSmile.setOnClickListenerWithTrigger {
            if (postion < smileList.size - 1) {
                postion++
                setShow()
            } else {
                getData()
            }
        }

        tvShowSmile.setOnLongClickListener(View.OnLongClickListener {
            MyUtils.copyStr(this, tvShowSmile.text.toString())
            showToast("已复制")
            return@OnLongClickListener true
        })

    }

    private fun getData() {
        showProDialog()
        DataManager.getInstances().apiService.smileList(JUHE_SMILE_KEY)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(MyObserver(object : ResponseCallBack<ResultData<List<SmileData>>> {
                override fun onSuccess(t: ResultData<List<SmileData>>) {
                    dismissProDialog()
                    smileList.addAll(t.result)
                    postion++
                    setShow()
                }

                override fun onFail(code: String, errorMsg: String) {
                    dismissProDialog()
                }

            }))
    }

    private fun setShow() {
        tvShowSmile.text = smileList[postion].content
    }

}