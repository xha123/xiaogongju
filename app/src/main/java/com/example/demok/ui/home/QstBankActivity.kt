package com.example.demok.ui.home

import android.app.Activity
import android.content.Intent
import android.view.View
import com.example.baselib.utils.setOnClickListenerWithTrigger
import com.example.baselib.utils.showToast
import com.example.demok.R
import com.example.demok.base.MyBaseActivity
import kotlinx.android.synthetic.main.activity_qstbank.*

/**
 * @author: xha
 * @date: 2020/10/10 15:17
 * @Description:驾照考试选择页面
 */
class QstBankActivity : MyBaseActivity(), View.OnClickListener {

    private var chooseOne = -1
    private var subOne = -1
    private val carTypeList = arrayOf("c1", "c2", "b1", "b2", "a1", "a2")
    private var carShow = ""
    private var subShow = ""
    private var numTopic = 100
    private var numberType = 2

    companion object {
        @JvmStatic
        fun start(
            context: Activity
        ) {
            val starter = Intent(context, QstBankActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_qstbank
    }

    override fun initOnCreate() {
        setTitleText("驾照考试")

        tvNumber3Qstbank.isSelected = true
    }

    override fun initOnClickerListener() {
        super.initOnClickerListener()


        tvShowC1.setOnClickListenerWithTrigger(this)
        tvShowC2.setOnClickListenerWithTrigger(this)
        tvShowB1.setOnClickListenerWithTrigger(this)
        tvShowB2.setOnClickListenerWithTrigger(this)
        tvShowA1.setOnClickListenerWithTrigger(this)
        tvShowA2.setOnClickListenerWithTrigger(this)
        tvShowSub1.setOnClickListenerWithTrigger(this)
        tvShowSub4.setOnClickListenerWithTrigger(this)
        tvStartQstBank.setOnClickListenerWithTrigger(this)
        tvNumber1Qstbank.setOnClickListenerWithTrigger(this)
        tvNumber2Qstbank.setOnClickListenerWithTrigger(this)
        tvNumber3Qstbank.setOnClickListenerWithTrigger(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvShowC1 -> {
                carTypeShow(false)
                chooseOne = 0
                carShow = tvShowC1.text.toString()
                carTypeShow(true)
            }
            R.id.tvShowC2 -> {
                carTypeShow(false)
                chooseOne = 1
                carShow = tvShowC2.text.toString()
                carTypeShow(true)
            }
            R.id.tvShowB1 -> {
                carTypeShow(false)
                chooseOne = 2
                carShow = tvShowB1.text.toString()
                carTypeShow(true)
            }
            R.id.tvShowB2 -> {
                carTypeShow(false)
                chooseOne = 3
                carShow = tvShowB2.text.toString()
                carTypeShow(true)
            }
            R.id.tvShowA1 -> {
                carTypeShow(false)
                chooseOne = 4
                carShow = tvShowA1.text.toString()
                carTypeShow(true)
            }
            R.id.tvShowA2 -> {
                carTypeShow(false)
                chooseOne = 5
                carShow = tvShowA2.text.toString()
                carTypeShow(true)
            }
            R.id.tvShowSub1 -> {
                subTypeShow(false)
                subOne = 1
                subShow = tvShowSub1.text.toString()
                subTypeShow(true)
            }
            R.id.tvShowSub4 -> {
                subTypeShow(false)
                subOne = 4
                subShow = tvShowSub4.text.toString()
                subTypeShow(true)
            }
            R.id.tvStartQstBank -> {
                checkInput()
            }
            R.id.tvNumber1Qstbank -> {
                numTopic = 20
                numberTypeShow(false)
                numberType = 0
                numberTypeShow(true)
            }
            R.id.tvNumber2Qstbank -> {
                numTopic = 50
                numberTypeShow(false)
                numberType = 1
                numberTypeShow(true)
            }
            R.id.tvNumber3Qstbank -> {
                numTopic = 100
                numberTypeShow(false)
                numberType = 2
                numberTypeShow(true)
            }
        }
    }

    private fun checkInput() {
        if (chooseOne == -1) {
            showToast("请选择车型")
            return
        }
        if (subOne == -1) {
            showToast("请选择科目")
            return
        }

        QstListActivity.start(
            this, "$carShow-$subShow",
            subOne.toString(), carTypeList[chooseOne], numTopic
        )
        finish()
    }

    fun subTypeShow(isSelect: Boolean) {
        when (subOne) {
            1 -> {
                tvShowSub1.isSelected = isSelect
            }
            4 -> {
                tvShowSub4.isSelected = isSelect
            }
        }
    }

    fun numberTypeShow(isSelect: Boolean) {
        when (numberType) {
            0 -> {
                tvNumber1Qstbank.isSelected = isSelect
            }
            1 -> {
                tvNumber2Qstbank.isSelected = isSelect
            }
            2 -> {
                tvNumber3Qstbank.isSelected = isSelect
            }
        }
    }

    fun carTypeShow(isSelect: Boolean) {
        when (chooseOne) {
            0 -> {
                tvShowC1.isSelected = isSelect
            }
            1 -> {
                tvShowC2.isSelected = isSelect
            }
            2 -> {
                tvShowB1.isSelected = isSelect
            }
            3 -> {
                tvShowB2.isSelected = isSelect
            }
            4 -> {
                tvShowA1.isSelected = isSelect
            }
            5 -> {
                tvShowA2.isSelected = isSelect
            }
        }
    }

}