package com.example.demok.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.recyclerview.widget.RecyclerView
import com.example.demok.base.MyApplication
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.*

/**
 * @author: xha
 * @date: 2020/10/12 9:58
 * @Description: 滚动recyclerview
 */

@Suppress("UNREACHABLE_CODE")
class RollRecyclerView : RecyclerView {

    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {

    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
    }

    init {

    }

    override fun onInterceptHoverEvent(event: MotionEvent?): Boolean {
        return super.onInterceptHoverEvent(event)

    }

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        return super.onInterceptTouchEvent(e)

    }

    var subscribe: Disposable? = null

    var timer: Timer? = null
    var timerTask: TimerTask? = null
    fun startMove() {
        if (timer != null) {
            timer!!.cancel()
        }
        timer = Timer()
        timerTask = object : TimerTask() {
            @SuppressLint("CheckResult")
            override fun run() {
                Observable.just(0)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { integer: Int? ->
                        smoothScrollBy(
                            20,
                            0
                        )
                    }
            }
        }
        timer!!.schedule(timerTask, 1000, 100)
    }

    fun stopMove() {
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
    }

    private var inputX = 0f
    private var inputY = 0f
    private var moveX = 0f
    private var moveY = 0f

    var touchSlop = ViewConfiguration.get(MyApplication.mContext).scaledTouchSlop

    override fun onTouchEvent(e: MotionEvent?): Boolean {
        return super.onTouchEvent(e)
        when (e?.action) {
            MotionEvent.ACTION_DOWN -> {
                stopMove()
                inputX = e.x
                inputY = e.y
            }
            MotionEvent.ACTION_MOVE -> {
                stopMove()

            }
            MotionEvent.ACTION_UP -> {
                moveX = e.x
                moveY = e.y
                if (Math.abs(x - moveX) > touchSlop || Math.abs(y - moveY) > touchSlop) {
                    startMove()
                }
            }
        }
    }
}