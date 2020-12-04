package com.example.demok.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.demok.R

/**
 * @author: xha
 * @date: 2020/10/22 15:41
 * @Description:
 */
class MyScanTextView : View {

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

    private var paint: Paint = Paint()
    private var pointX = 0f
    private var textWidth = 0f
    private var animatorset: AnimatorSet
    var textShow = ""

    fun setPointX(pointX: Float) {
        this.pointX = pointX
        invalidate()
    }

    init {
        paint.color = ContextCompat.getColor(context, R.color.color_red)
        animatorset = AnimatorSet()
        paint.textSize = 30f
        paint.isAntiAlias = true
        pointX = width.toFloat()
        textShow = "测试横屏动画效果"
        textWidth = paint.measureText(textShow)
        this.visibility = View.INVISIBLE
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val centerX = width / 2.toFloat()
        val centerY = height / 2.toFloat()

        textWidth = paint.measureText(textShow)

        paint.setStyle(Paint.Style.FILL); // 填充模式
        paint.color = Color.parseColor("#9B27AF")


        canvas?.drawText(textShow, pointX, centerY, paint)
    }

    @SuppressLint("AnimatorKeep")
    fun startAnim() {
        this.visibility = View.VISIBLE
        if (animatorset.isRunning) {
            animatorset.cancel()
        }
        val centerX = measuredWidth / 2
        val centerY = measuredHeight / 2

        val enterAnimtor =
            ObjectAnimator.ofFloat(
                this,
                "pointX",
                measuredWidth.toFloat(),
                centerX.toFloat() - textWidth / 2
            )
                .setDuration(1000)
        val enterAnimator2 =
            ObjectAnimator.ofFloat(this, "alpha", 1.0f, 1.0f)
                .setDuration(2000)
        val enterAnimator3 =
            ObjectAnimator.ofFloat(
                this,
                "pointX",
                centerX.toFloat() - textWidth / 2,
                -textWidth
            )
                .setDuration(1000)
        animatorset.playSequentially(enterAnimtor, enterAnimator2, enterAnimator3)
        animatorset.start()

    }
}