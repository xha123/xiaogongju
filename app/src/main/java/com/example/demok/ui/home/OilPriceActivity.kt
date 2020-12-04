package com.example.demok.ui.home


import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.view.View
import com.example.baselib.utils.Constants.TIME_HOUR
import com.example.baselib.utils.SharedPreferencesUtils
import com.example.baselib.utils.setOnClickListenerWithTrigger
import com.example.baselib.utils.showToast
import com.example.demok.R
import com.example.demok.base.MyBaseActivity
import com.example.demok.net.DataManager
import com.example.demok.net.MyObserver
import com.example.demok.net.ResponseCallBack
import com.example.demok.net.data.OilPriceData
import com.example.demok.net.data.ResultData
import com.example.demok.utils.Const
import com.example.demok.utils.Const.JUHE_OIL_KEY
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_oil_price.*
import kotlinx.coroutines.launch
import org.w3c.dom.Text


/**
 * @author: xha
 * @date: 2020/10/10 13:50
 * @Description:今日油价
 */
class OilPriceActivity : MyBaseActivity() {

    companion object {
        @JvmStatic
        fun start(
            context: Activity
        ) {
            val starter = Intent(context, OilPriceActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_oil_price
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun initOnCreate() {
        setTitleText("今日油价")

        val oilData = SharedPreferencesUtils.getShared(Const.Shared.OIL_PRICE_DATA, "") as String
        if (oilData.isNullOrEmpty()) {
            getData()
        } else {
            val oilPriceData: OilPriceData = Gson().fromJson(oilData, OilPriceData::class.java)
            setShow(oilPriceData)
        }

        tvUpdateOilPrice.setOnClickListenerWithTrigger {
            val oldTime =
                SharedPreferencesUtils.getShared(Const.Shared.OIL_PRICE_TIME, 0L) as Long
            if (System.currentTimeMillis() - oldTime < TIME_HOUR) {
                showToast("已是最新数据")
                tvUpdateOilPrice.visibility = View.GONE
            } else {
                getData()
            }
        }
    }

    private fun setShow(oilPriceData: OilPriceData) {
        val typeface = Typeface.createFromAsset(
            assets,
            "LESLIEB_.TTF"
        )
        tvShow92.typeface = typeface
        tvShow95.typeface = typeface
        tvShow98.typeface = typeface
        tvShow0.typeface = typeface

        tvShow92.text = oilPriceData.show92
        tvShow95.text = oilPriceData.show95
        tvShow98.text = oilPriceData.show98
        tvShow0.text = oilPriceData.show0

        // 构建GradientDrawable对象并设置属性
        val drawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE // 矩形
            cornerRadius = 10f // 圆角
            colors = intArrayOf(Color.parseColor("#ff00ff"),Color.parseColor("#800000ff")) //渐变色
            gradientType = GradientDrawable.LINEAR_GRADIENT // 渐变类型
            orientation = GradientDrawable.Orientation.LEFT_RIGHT // 渐变方向
            setStroke(2,Color.parseColor("#ffff00")) // 描边宽度和颜色
        }
        // 将GradientDrawable对象设置为控件背景
        tvShow0?.background = drawable

    }

    fun getData1() {
        launch {
            val result =
                DataManager.getInstances().apiService.oilPriceList1(JUHE_OIL_KEY)
        }
    }

    private fun getData() {
        showProDialog()
        DataManager.getInstances().apiService.oilPriceList(JUHE_OIL_KEY)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(MyObserver(object : ResponseCallBack<ResultData<List<OilPriceData>>> {
                override fun onSuccess(t: ResultData<List<OilPriceData>>) {
                    dismissProDialog()
                    t.result.forEach {
                        if (it.city.contains("四川") || it.city.contains("成都")) {
                            setShow(it)
                            val str = Gson().toJson(it)
                            SharedPreferencesUtils.putShared(Const.Shared.OIL_PRICE_DATA, str)
                            SharedPreferencesUtils.putShared(
                                Const.Shared.OIL_PRICE_TIME,
                                System.currentTimeMillis()
                            )
                            return@forEach
                        }
                    }
                }

                override fun onFail(code: String, errorMsg: String) {
                    dismissProDialog()
                }

            }))
    }

}