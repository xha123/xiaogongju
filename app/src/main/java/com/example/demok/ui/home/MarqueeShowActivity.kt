package com.example.demok.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import com.example.baselib.utils.LogUtils
import com.example.demok.R
import com.example.demok.base.MyBaseActivity
import com.example.demok.utils.Const
import com.example.demok.utils.MyUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_marqueeshow.*
import master.flame.danmaku.controller.DrawHandler
import master.flame.danmaku.danmaku.model.BaseDanmaku
import master.flame.danmaku.danmaku.model.DanmakuTimer
import master.flame.danmaku.danmaku.model.IDanmakus
import master.flame.danmaku.danmaku.model.android.DanmakuContext
import master.flame.danmaku.danmaku.model.android.Danmakus
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser
import java.util.concurrent.TimeUnit


/**
 * @author: xha
 * @date: 2020/10/15 17:41
 * @Description:跑马灯效果展示页面
 */
class MarqueeShowActivity : MyBaseActivity() {

    var str = ""
    var isHor = false

    companion object {
        @JvmStatic
        fun start(
            context: Activity,
            str: String,
            isHor: Boolean? = false
        ) {
            val starter = Intent(context, MarqueeShowActivity::class.java)
                .putExtra(Const.Intent.DATA, str)
                .putExtra(Const.Intent.IS_HOR, isHor)
            context.startActivity(starter)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_marqueeshow
    }

    override fun initOnCreate() {
        showHeader(false)
        showTitle(false)

        str = intent.getStringExtra(Const.Intent.DATA) ?: ""
        isHor = intent.getBooleanExtra(Const.Intent.IS_HOR, false)

        if (isHor) {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }

        setDanMaku()

    }

    private val danmakuContext: DanmakuContext by lazy {
        DanmakuContext.create()
    }
    private var showDanmaku = false
    private val parser: BaseDanmakuParser = object : BaseDanmakuParser() {
        override fun parse(): IDanmakus {
            return Danmakus()
        }
    }

    @SuppressLint("CheckResult")
    private fun setDanMaku() {

        danmakuShowMarquee.enableDanmakuDrawingCache(true);
        danmakuShowMarquee.setCallback(object : DrawHandler.Callback {
            override fun prepared() {
                showDanmaku = true
                danmakuShowMarquee.start()
            }

            override fun updateTimer(timer: DanmakuTimer?) {

            }

            override fun danmakuShown(danmaku: BaseDanmaku?) {

            }

            override fun drawingFinished() {
                addText()
            }
        })

//        danmakuShowMarquee.seekTo(20000)
        danmakuContext.setScrollSpeedFactor(3f)
        danmakuShowMarquee.prepare(parser, danmakuContext);

        Observable.timer(1000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(Consumer {
                addText()
            })

    }

    private fun addText() {

        val danmaku: BaseDanmaku =
            danmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL)
        danmaku.text = str
        danmaku.padding = 5
        danmaku.textSize = MyUtils.sp2px(this, 240F).toFloat()
        if (themeInt == 0) {
            danmaku.textColor = getColor(R.color.color_333333)
        } else {
            danmaku.textColor = getColor(R.color.color_white)
        }

        danmaku.time = danmakuShowMarquee.currentTime
        LogUtils.i("弹幕时间" + danmaku.time)

        danmakuShowMarquee.addDanmaku(danmaku)
    }

    override fun onResume() {
        super.onResume()
        if (danmakuShowMarquee != null && danmakuShowMarquee.isPrepared() && danmakuShowMarquee.isPaused()) {
            danmakuShowMarquee.resume();
        }
    }

    override fun onPause() {
        super.onPause()
        if (danmakuShowMarquee != null && danmakuShowMarquee.isPrepared()) {
            danmakuShowMarquee.pause();
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (danmakuShowMarquee != null) {
            danmakuShowMarquee.release();
        }
    }

}