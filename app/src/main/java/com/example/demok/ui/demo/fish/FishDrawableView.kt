package com.example.demok.ui.demo.fish

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.os.Parcel
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import java.util.*


/**
 * @author: xha
 * @date: 2020/10/19 15:17
 * @Description:
 */
@SuppressLint("ParcelCreator")
class FishDrawableView(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
    RelativeLayout(context, attrs, defStyleAttr) {
    private var mScreenWidth = 0
    private var mScreenHeight = 0
    private lateinit var ivFish: ImageView
    private lateinit var fishDrawable: FishDrawable
    private lateinit var animator: ObjectAnimator
    private lateinit var rippleAnimator: ObjectAnimator
    private lateinit var mPaint: Paint
    private var alpha = 100
    private lateinit var canvas: Canvas
    private var moveX = 0f
    private var moveY = 0f
    private var radius = 0f

    constructor(parcel: Parcel) : this(
        TODO("context"),
        TODO("attrs"),
        TODO("defStyleAttr")
    ) {
        mScreenWidth = parcel.readInt()
        mScreenHeight = parcel.readInt()
        alpha = parcel.readInt()
        moveX = parcel.readFloat()
        moveY = parcel.readFloat()
        radius = parcel.readFloat()
    }

    constructor(context: Context?) : this(context, null) {}
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(mScreenWidth, mScreenHeight)
    }

    private fun initStuff(context: Context?) {
        setWillNotDraw(false)
        getScreenParams()
        mPaint = Paint()
        mPaint.setAntiAlias(true)
        mPaint.setDither(true)
        mPaint.setStyle(Paint.Style.STROKE)
        mPaint.setStrokeWidth(STROKE_WIDTH.toFloat())
        ivFish = ImageView(context)
        val params =
            LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        ivFish.setLayoutParams(params)
        fishDrawable = FishDrawable(context)
        ivFish.setImageDrawable(fishDrawable)
        addView(ivFish)
    }

    override fun onDraw(canvas: Canvas) {
        this.canvas = canvas
        //方便刷新透明度
        mPaint.setARGB(alpha, 0, 125, 251)
        canvas.drawCircle(moveX, moveY, radius, mPaint)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onTouchEvent(event: MotionEvent): Boolean {
        moveX = event.x
        moveY = event.y
        rippleAnimator = ObjectAnimator.ofFloat(this, "radius", 0f, 1f).setDuration(1000)
        rippleAnimator!!.start()
        makeTrail(PointF(moveX, moveY))
        return super.onTouchEvent(event)
    }

    /**
     * 鱼头是第一控点，中点和头与中点和点击点的夹角的一半是第二个控制点角度
     *
     * @param touch
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun makeTrail(touch: PointF) {
        /**
         * 起点和中点是鱼的身体重心处和点击点，三阶贝塞尔的两个控制点分别是鱼头圆心点和夹角中点。
         * 但是平移不是平移的鱼，而是平移的ImageView，而ImageView的setX，setY方法是相对于ImageView左上角的
         * 所以需要把计算好的贝塞尔曲线做一个平移，让贝塞尔曲线的起点和ImageView重合
         * deltaX,Y即是平移的绝对距离
         */
        val deltaX: Float = fishDrawable.getMiddlePoint()!!.x
        val deltaY: Float = fishDrawable.getMiddlePoint()!!.y
        val path = Path()
        val fishMiddle = PointF(ivFish.getX() + deltaX, ivFish.getY() + deltaY)
        val fishHead = PointF(
            ivFish.getX() + fishDrawable.headPoint.x,
            ivFish.getY() + fishDrawable.headPoint.y
        )
        //把贝塞尔曲线起始点平移到Imageview的左上角
        path.moveTo(fishMiddle.x - deltaX, fishMiddle.y - deltaY)
        val angle = includedAngle(fishMiddle, fishHead, touch)
        val delta = calcultatAngle(fishMiddle, fishHead)
        val controlF =
            calculatPoint(fishMiddle, 1.6f * fishDrawable!!.HEAD_RADIUS, angle / 2 + delta)
        //把贝塞尔曲线的所有控制点和结束点都做平移处理
        path.cubicTo(
            fishHead.x - deltaX,
            fishHead.y - deltaY,
            controlF.x - deltaX,
            controlF.y - deltaY,
            touch.x - deltaX,
            touch.y - deltaY
        )
        val tan = FloatArray(2)
        val pathMeasure = PathMeasure(path, false)
        animator = ObjectAnimator.ofFloat(ivFish, "x", "y", path)
        animator.setDuration(2 * 1000.toLong())
        animator.setInterpolator(AccelerateDecelerateInterpolator())
        //动画启动和结束时设置鱼鳍摆动动画，同时控制鱼身摆动频率
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                fishDrawable!!.setWaveFrequence(1f)
            }

            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
                fishDrawable!!.setWaveFrequence(2f)
                val finsAnimator = fishDrawable!!.finsAnimator
                finsAnimator!!.repeatCount = Random().nextInt(3)
                finsAnimator.duration = (((Random().nextInt(1) + 1) * 500).toLong())
                finsAnimator.start()
            }
        })
        animator.addUpdateListener(AnimatorUpdateListener { animation ->
            val fraction = animation.animatedFraction
            pathMeasure.getPosTan(pathMeasure.length * fraction, null, tan)
            val angle = Math.toDegrees(
                Math.atan2(
                    (-tan[1]).toDouble(),
                    tan[0].toDouble()
                )
            ).toFloat()
            Log.e("**-**-**-**", "onAnimationUpdate: $angle")
            fishDrawable.mainAngle = (angle)
        })
        animator.start()
    }

    /**
     * ObjectAnimators自动执行
     *
     * @param currentValue
     */
    fun setRadius(currentValue: Float) {
        alpha = (100 * (1 - currentValue) / 2).toInt()
        radius = DEFAULT_RADIUS * currentValue
        invalidate()
    }

    /**
     * 获取屏幕宽高
     */
    fun getScreenParams() {
        val WM = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val mDisplayMetrics = DisplayMetrics()
        WM.defaultDisplay.getMetrics(mDisplayMetrics)
        mScreenWidth = mDisplayMetrics.widthPixels
        mScreenHeight = mDisplayMetrics.heightPixels
    }

    companion object {
        const val STROKE_WIDTH = 8
        const val DEFAULT_RADIUS = 150f

        /**
         * 起点\长度\角度计算终点
         * 正逆负顺
         *
         * @param startPoint
         * @param length
         * @param angle
         * @return
         */
        private fun calculatPoint(startPoint: PointF, length: Float, angle: Float): PointF {
            val deltaX = Math.cos(Math.toRadians(angle.toDouble()))
                .toFloat() * length
            //符合Android坐标的y轴朝下的标准
            val deltaY = Math.sin(Math.toRadians(angle - 180.toDouble()))
                .toFloat() * length
            return PointF(startPoint.x + deltaX, startPoint.y + deltaY)
        }

        /**
         * 线和x轴夹角
         *
         * @param start
         * @param end
         * @return
         */
        fun calcultatAngle(start: PointF, end: PointF): Float {
            return includedAngle(start, PointF(start.x + 1, start.y), end)
        }

        /**
         * 利用向量的夹角公式计算夹角
         * cosBAC = (AB*AC)/(|AB|*|AC|)
         * 其中AB*AC是向量的数量积AB=(Bx-Ax,By-Ay)  AC=(Cx-Ax,Cy-Ay),AB*AC=(Bx-Ax)*(Cx-Ax)+(By-Ay)*(Cy-Ay)
         *
         * @param center 顶点 A
         * @param head   点1  B
         * @param touch  点2  C
         * @return
         */
        fun includedAngle(center: PointF, head: PointF, touch: PointF): Float {
            val abc =
                (head.x - center.x) * (touch.x - center.x) + (head.y - center.y) * (touch.y - center.y)
            val angleCos = (abc /
                    (Math.sqrt((head.x - center.x) * (head.x - center.x) + (head.y - center.y) * (head.y - center.y).toDouble())
                            * Math.sqrt((touch.x - center.x) * (touch.x - center.x) + (touch.y - center.y) * (touch.y - center.y).toDouble()))).toFloat()
            println(angleCos.toString() + "angleCos")
            val temAngle =
                Math.toDegrees(Math.acos(angleCos.toDouble())).toFloat()
            //判断方向  正左侧  负右侧 0线上,但是Android的坐标系Y是朝下的，所以左右颠倒一下
            val direction =
                (center.x - touch.x) * (head.y - touch.y) - (center.y - touch.y) * (head.x - touch.x)
            var showFloat: Float = 0f
            if (direction == 0f) {
                if (abc >= 0) {
                    showFloat = 0f
                } else {
                    showFloat = 180f
                }
            } else {
                if (direction > 0) { //右侧顺时针为负
                    showFloat - temAngle
                } else {
                    showFloat = temAngle
                }
            }
            return showFloat
        }
    }

    init {
        initStuff(context)
    }

}