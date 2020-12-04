package com.example.demok.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import com.example.baselib.utils.setOnClickListenerWithTrigger
import com.example.demok.R
import com.example.demok.base.MyBaseActivity
import kotlinx.android.synthetic.main.activity_qstreult.*

/**
 * @author: xha
 * @date: 2020/10/12 15:45
 * @Description: 考试结果
 */
class QstReulstActivity : MyBaseActivity() {

    var title: String = ""
    var subject: String = ""
    var model: String = ""
    var score: Int = 0
    var numTopic: Int = 100
    var timeShow: String = ""

    companion object {
        @JvmStatic
        fun start(
            context: Activity,
            title: String,
            subject: String,
            model: String,
            score: Int,
            numTopic: Int,
            timeShow: String
        ) {
            val starter = Intent(context, QstReulstActivity::class.java)
                .putExtra("title", title)
                .putExtra("subject", subject)
                .putExtra("model", model)
                .putExtra("score", score)
                .putExtra("numTopic", numTopic)
                .putExtra("timeShow", timeShow)
            context.startActivity(starter)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_qstreult
    }

    @SuppressLint("SetTextI18n")
    override fun initOnCreate() {
        title = intent.getStringExtra("title") ?: ""
        subject = intent.getStringExtra("subject") ?: ""
        model = intent.getStringExtra("model") ?: ""
        timeShow = intent.getStringExtra("timeShow") ?: ""
        score = intent.getIntExtra("score", 0)
        numTopic = intent.getIntExtra("numTopic", 100)
        setTitleText("考试结束")


        if (score >= 90) {
            tvShowQstreult.text = "考试结束，成绩合格"
        } else {
            tvShowQstreult.text = "考试结束，不成绩合格"
        }
        tvShow2Qstreult.text = "用时 ${timeShow}"
        tvShow1Qstreult.text = "${score}分"

        tvAgainQstresult.setOnClickListenerWithTrigger {
            QstListActivity.start(this, title, subject, model, numTopic)
        }

        tvOverQstresult.setOnClickListenerWithTrigger {
            finish()
        }
    }
}