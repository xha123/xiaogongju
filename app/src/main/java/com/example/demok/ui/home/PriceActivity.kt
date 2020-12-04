package com.example.demok.ui.home

import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import com.example.baselib.utils.setOnClickListenerWithTrigger
import com.example.baselib.utils.showToast
import com.example.demok.R
import com.example.demok.base.MyBaseActivity
import com.example.demok.databinding.ActivityPriceOneBinding
import com.example.demok.net.data.PriceModel
import com.example.demok.utils.MyUtils
import kotlinx.android.synthetic.main.activity_price_one.*
import javax.inject.Inject

/**
 * @author: xha
 * @date: 2020/11/6 11:06
 * @Description:比价计算器
 */
class PriceActivity : MyBaseActivity() {

    @Inject
    lateinit var priceModel1: PriceModel

    companion object {
        @JvmStatic
        fun start(
            context: Activity
        ) {
            val starter = Intent(context, PriceActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun getLayoutId(): Int? {
//        return R.layout.activity_price
        return null
    }

    override fun initOnCreate() {
        val binder: ActivityPriceOneBinding =
            DataBindingUtil.setContentView(this@PriceActivity, R.layout.activity_price_one);
        setTitleText("价格计算器")

        priceModel1 = PriceModel("商品1")

        binder.priceModel1 = priceModel1

        priceModel1.title = "商品一"
    }

    override fun initOnClickerListener() {
        super.initOnClickerListener()

        setEditTextChanngedListener(edtprice1)
        setEditTextChanngedListener(edtprice2)
        setEditTextChanngedListener(edtUnitPrice1)
        setEditTextChanngedListener(edtUnitPrice2)

        tvPrice.setOnClickListenerWithTrigger {
            checkInput()
        }
    }

    private fun checkInput() {
        val price1 = edtprice1.text.toString()
        val price2 = edtprice2.text.toString()
        val unit1 = edtUnitPrice1.text.toString()
        val unit2 = edtUnitPrice2.text.toString()

        if (price1.isNullOrEmpty()) {
            showToast("请输入商品价格及单位")
            return
        }
        if (price2.isNullOrEmpty()) {
            showToast("请输入商品价格及单位")
            return
        }
        if (unit1.isNullOrEmpty()) {
            showToast("请输入商品价格及单位")
            return
        }
        if (unit2.isNullOrEmpty()) {
            showToast("请输入商品价格及单位")
            return
        }

        priceModel1.price = price1
        priceModel1.unit = unit1

        val diff1 = priceModel1.getResult()

        priceModel1.price = price2
        priceModel1.unit = unit2
        val diff2 = priceModel1.getResult()
        val diff = diff1 - diff2
        if (diff < 0) {
            priceModel1.endShow = "我更便宜哟！"
            tvwinPrice1.visibility = View.VISIBLE
            tvwinPrice2.visibility = View.INVISIBLE
        } else if (diff > 0) {
            priceModel1.endShow = "我更便宜啦"
            tvwinPrice1.visibility = View.INVISIBLE
            tvwinPrice2.visibility = View.VISIBLE
        } else {
            tvwinPrice1.visibility = View.VISIBLE
            tvwinPrice2.visibility = View.VISIBLE
            priceModel1.endShow = "都不买更划算哟"
        }
    }


    private fun setEditTextChanngedListener(edt: EditText) {
        edt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                val inputStr = edt.text.toString()
                if (inputStr.isNotEmpty()) {
                    if (inputStr.contains(".")) {//如果有.检测是否是小数
                        if (!MyUtils.isNumberFor2(inputStr)) {
                            edt.setText(
                                inputStr.substring(
                                    0,
                                    inputStr.length - 1
                                )
                            )
                            edt.setSelection(inputStr.length - 1)
                        }
                    } else {
                        if (!MyUtils.isNumber(inputStr)) {
                            edt.setText(
                                inputStr.substring(
                                    0,
                                    inputStr.length - 1
                                )
                            )
                            edt.setSelection(inputStr.length - 1)
                        }
                    }
                }
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }


}