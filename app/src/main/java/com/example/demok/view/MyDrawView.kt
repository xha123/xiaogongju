package com.example.demok.view

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
class MyDrawView : View {

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

    init {
        paint.color = ContextCompat.getColor(context, R.color.color_red)
        paint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        paint.setStyle(Paint.Style.FILL); // 填充模式
        paint.color = Color.parseColor("#9B27AF")
        canvas?.drawArc(110f, 110f, 610f, 610f,
            0f, 10f, true, paint); // 绘制扇形

        paint.color = Color.parseColor("#9D9D9D")
        canvas?.drawArc(100f, 100f, 600f, 600f,
            10f, 10f, true, paint); // 绘制扇形


        paint.color = Color.parseColor("#009587")
        canvas?.drawArc(100f, 100f, 600f, 600f,
            20f, 60f, true, paint); // 绘制扇形

        paint.color = Color.parseColor("#009587")
        canvas?.drawArc(100f, 100f, 600f, 600f,
            80f, 100f, true, paint); // 绘制扇形

        paint.color = Color.parseColor("#2195F2")
        canvas?.drawArc(100f, 100f, 600f, 600f,
            80f, 100f, true, paint); // 绘制扇形

        paint.color = Color.parseColor("#F34336")
        canvas?.drawArc(90f, 90f, 590f, 590f,
            180f, 120f, true, paint); // 绘制扇形


        paint.color = Color.parseColor("#FDC007")
        canvas?.drawArc(100f, 100f, 600f, 600f,
            300f, 60f, true, paint); // 绘制扇形

    }

}