package com.example.demok.ui.demo.fish

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.view.animation.LinearInterpolator
import java.util.*


/**
 * @author: xha
 * @date: 2020/10/19 14:35
 * @Description:
 */
open class FishDrawable(context: Context?) : Drawable() {

    private val TAG = "Jcs_Fishsss"
    val HEAD_RADIUS = 20f
    private val BODY_LENGHT: Float = HEAD_RADIUS * 3.2f //第一节身体长度

    private val BODY_ALPHA = 220
    private val OTHER_ALPHA = 160
    private val FINS_ALPHA = 100
    private val FINS_LEFT = 1 //左鱼鳍

    private val FINS_RIGHT = -1
    private val FINS_LENGTH: Float = HEAD_RADIUS * 1.3f
    val TOTAL_LENGTH: Float = 6.79f * HEAD_RADIUS

    private lateinit var mPaint: Paint
    private var mContext: Context? = null

    //控制区域
    private var currentValue = 0 //全局控制标志

    var mainAngle = 90f //角度表示的角

    var finsAnimator: ObjectAnimator? = null
    private var waveFrequence = 1f

    //鱼头点
    public lateinit var headPoint: PointF

    //转弯更自然的中心点
    private lateinit var middlePoint: PointF
    private var finsAngle = 0f
    private var bodyPaint: Paint? = null
    private lateinit var mPath: Path


    override fun draw(canvas: Canvas) {
        //生成一个半透明图层，否则与背景白色形成干扰,尺寸必须与view的大小一致否则鱼显示不全
        val saveLayerAlpha = canvas.saveLayerAlpha(
            0f,
            0f,
            bounds.width().toFloat(),
            bounds.height().toFloat(),
            240
        );
        makeBody(canvas, HEAD_RADIUS);
        canvas.restore();
        mPath.reset();
        mPaint.color = Color.argb(OTHER_ALPHA, 244, 92, 71);
    }

    //设置Drawable的透明度，一般情况下将此alpha值设置给Paint
    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha;
    }

    //设置颜色滤镜，一般情况下将此值设置给Paint
    override fun setColorFilter(colorFilter: ColorFilter?) {
        mPaint.colorFilter = colorFilter;
    }

    //决定绘制的部分是否遮住Drawable下边的东西，有点抽象，有几种模式
    //PixelFormat.UNKNOWN
    //PixelFormat.TRANSLUCENT 只有绘制的地方才盖住下边
    //PixelFormat.TRANSPARENT 透明，不显示绘制内容
    //PixelFormat.OPAQUE 完全盖住下边内容
    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT;
    }

    override fun getIntrinsicHeight(): Int {
//        return super.getIntrinsicHeight()
        return (8.38f * HEAD_RADIUS).toInt()
    }

    override fun getIntrinsicWidth(): Int {
//        return super.getIntrinsicWidth()
        return (8.38f * HEAD_RADIUS).toInt()
    }

    init {
        initDrawable()
    }

    private fun initDrawable() {

        //路径
        mPath = Path()
        //画笔
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.FILL
        mPaint.isDither = true //防抖
        mPaint.color = Color.argb(OTHER_ALPHA, 244, 92, 71)
        //身体画笔
        bodyPaint = Paint()
        bodyPaint?.isAntiAlias = true
        bodyPaint?.style = Paint.Style.FILL
        bodyPaint?.isDither = true //防抖
        bodyPaint?.color = Color.argb(OTHER_ALPHA + 5, 244, 92, 71)
        middlePoint = PointF(4.18f * HEAD_RADIUS, 4.18f * HEAD_RADIUS)

        //鱼鳍灵动动画
        finsAnimator = ObjectAnimator.ofFloat(this, "finsAngle", 0f, 1f, 0f)
        finsAnimator?.repeatMode = ValueAnimator.REVERSE
        finsAnimator?.repeatCount = Random().nextInt(3)

        //引擎部分
        val valueAnimator = ValueAnimator.ofInt(0, 540 * 100)
        valueAnimator.duration = 180 * 1000.toLong()
        valueAnimator.interpolator = LinearInterpolator()
        valueAnimator.repeatCount = ValueAnimator.INFINITE
        valueAnimator.repeatMode = ValueAnimator.REVERSE
        valueAnimator.addUpdateListener { animation ->
            currentValue = animation.animatedValue as Int
            invalidateSelf()
        }
        valueAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationRepeat(animation: Animator) {
                super.onAnimationRepeat(animation)
                finsAnimator?.start()
            }
        })
        valueAnimator.start()
    }


    /**
     *  输入起点、长度、旋转角度计算终点
     *  @param startPointF 起点
     *  @param length 长度
     *  @param angle 选择角度
     */
    private fun calculatPoint(startPointF: PointF, length: Float, angle: Float): PointF {
        val deltaX = Math.cos(Math.toRadians(angle.toDouble())) * length
        //符合Android坐标的y轴朝下的标准
        val delteY = Math.sin(Math.toRadians((angle - 180).toDouble())) * length
        return PointF((startPointF.x + deltaX).toFloat(), (startPointF.y + delteY).toFloat())
    }


    private fun makeBody(canvas: Canvas, headRadius: Float) {
        val angle: Float =
            (mainAngle + Math.sin(Math.toRadians(currentValue * 1.2 * waveFrequence)) * 2).toFloat()
        headPoint = calculatPoint(middlePoint!!, BODY_LENGHT / 2, mainAngle)
        //画头
        canvas.drawCircle(headPoint.x, headPoint.y, HEAD_RADIUS, mPaint);
        //右鳍 起点

        //右鳍 起点
        val pointFinsRight = calculatPoint(headPoint, headRadius * 0.9f, (angle - 110).toFloat())
        makeFins(canvas, pointFinsRight, FINS_RIGHT, angle)
        //左鳍 起点

        //左鳍 起点
        val pointFinsLeft = calculatPoint(headPoint, headRadius * 0.9f, (angle + 110).toFloat())
        makeFins(canvas, pointFinsLeft, FINS_LEFT, angle)

        val endPoint = calculatPoint(headPoint, BODY_LENGHT, (angle - 180).toFloat())
        //躯干2
        //躯干2
        val mainPoint = PointF(endPoint.x, endPoint.y)
        makeSegments(canvas, mainPoint, headRadius * 0.7f, 0.6f, angle)
        val point1: PointF
        val point2: PointF
        val point3: PointF
        val point4: PointF
        val contralLeft: PointF
        val contralRight: PointF
        //point1和4的初始角度决定发髻线的高低值越大越低
        //point1和4的初始角度决定发髻线的高低值越大越低
        point1 = calculatPoint(headPoint, headRadius, (angle - 80).toFloat())
        point2 = calculatPoint(endPoint, headRadius * 0.7f, (angle - 90).toFloat())
        point3 = calculatPoint(endPoint, headRadius * 0.7f, (angle + 90).toFloat())
        point4 = calculatPoint(headPoint, headRadius, (angle + 80).toFloat())
        //决定胖瘦
        //决定胖瘦
        contralLeft = calculatPoint(headPoint, BODY_LENGHT * 0.56f, (angle - 130).toFloat())
        contralRight = calculatPoint(headPoint, BODY_LENGHT * 0.56f, (angle + 130).toFloat())
        mPath!!.reset()
        mPath!!.moveTo(point1.x, point1.y)
        mPath!!.quadTo(contralLeft.x, contralLeft.y, point2.x, point2.y)
        mPath!!.lineTo(point3.x, point3.y)
        mPath!!.quadTo(contralRight.x, contralRight.y, point4.x, point4.y)
        mPath!!.lineTo(point1.x, point1.y)

        mPaint.color = Color.argb(BODY_ALPHA, 244, 92, 71)
        //画最大的身子
        //画最大的身子
        canvas.drawPath(mPath!!, mPaint)
    }

    /**
     * 第二节节肢
     * 0.7R * 0.6 =1.12R
     */
    private fun makeSegments(
        canvas: Canvas,
        mainPoint: PointF,
        segmentRadius: Float,
        MP: Float,
        fatherAngle: Float
    ) {
        val angle = fatherAngle + Math.cos(Math.toRadians(currentValue * 1.5 * waveFrequence))
            .toFloat() * 15 //中心轴线和X轴顺时针方向夹角
        //身长
        val segementLenght = segmentRadius * (MP + 1)
        val endPoint = calculatPoint(mainPoint, segementLenght, angle - 180)
        val point1: PointF
        val point2: PointF
        val point3: PointF
        val point4: PointF
        point1 = calculatPoint(mainPoint, segmentRadius, angle - 90)
        point2 = calculatPoint(endPoint, segmentRadius * MP, angle - 90)
        point3 = calculatPoint(endPoint, segmentRadius * MP, angle + 90)
        point4 = calculatPoint(mainPoint, segmentRadius, angle + 90)
        canvas.drawCircle(mainPoint.x, mainPoint.y, segmentRadius, mPaint)
        canvas.drawCircle(endPoint.x, endPoint.y, segmentRadius * MP, mPaint)
        mPath!!.reset()
        mPath.moveTo(point1.x, point1.y)
        mPath.lineTo(point2.x, point2.y)
        mPath.lineTo(point3.x, point3.y)
        mPath.lineTo(point4.x, point4.y)
        canvas.drawPath(mPath, mPaint)

        //躯干2
        val mainPoint2 = PointF(endPoint.x, endPoint.y)
        makeSegmentsLong(canvas, mainPoint2, segmentRadius * 0.6f, 0.4f, angle)
    }

    /**
     * 第三节节肢
     * 0.7R * 0.6 * (0.4 + 2.7) + 0.7R * 0.6 * 0.4=1.302R + 0.168R
     */
    private fun makeSegmentsLong(
        canvas: Canvas,
        mainPoint: PointF,
        segmentRadius: Float,
        MP: Float,
        fatherAngle: Float
    ) {
        val angle = fatherAngle + Math.sin(Math.toRadians(currentValue * 1.5 * waveFrequence))
            .toFloat() * 35 //中心轴线和X轴顺时针方向夹角
        //身长
        val segementLenght = segmentRadius * (MP + 2.7f)
        val endPoint = calculatPoint(mainPoint, segementLenght, angle - 180)
        val point1: PointF
        val point2: PointF
        val point3: PointF
        val point4: PointF
        point1 = calculatPoint(mainPoint, segmentRadius, angle - 90)
        point2 = calculatPoint(endPoint, segmentRadius * MP, angle - 90)
        point3 = calculatPoint(endPoint, segmentRadius * MP, angle + 90)
        point4 = calculatPoint(mainPoint, segmentRadius, angle + 90)
        makeTail(canvas, mainPoint, segementLenght, segmentRadius, angle)
        canvas.drawCircle(endPoint.x, endPoint.y, segmentRadius * MP, mPaint)
        mPath!!.reset()
        mPath.moveTo(point1.x, point1.y)
        mPath.lineTo(point2.x, point2.y)
        mPath.lineTo(point3.x, point3.y)
        mPath.lineTo(point4.x, point4.y)
        canvas.drawPath(mPath, mPaint)
    }

    /**
     * 鱼鳍
     *
     * @param canvas
     * @param startPoint
     * @param type
     */
    private fun makeFins(canvas: Canvas, startPoint: PointF, type: Int, fatherAngle: Float) {
        val contralAngle = 115f //鱼鳍三角控制角度
        mPath!!.reset()
        mPath.moveTo(startPoint.x, startPoint.y)
        val endPoint = calculatPoint(
            startPoint,
            FINS_LENGTH,
            if (type == FINS_RIGHT) fatherAngle - finsAngle - 180 else fatherAngle + finsAngle + 180
        )
        val contralPoint = calculatPoint(
            startPoint,
            FINS_LENGTH * 1.8f,
            if (type == FINS_RIGHT) fatherAngle - contralAngle - finsAngle else fatherAngle + contralAngle + finsAngle
        )
        mPath.quadTo(contralPoint.x, contralPoint.y, endPoint.x, endPoint.y)
        mPath.lineTo(startPoint.x, startPoint.y)
        mPaint.color = Color.argb(FINS_ALPHA, 244, 92, 71)
        canvas.drawPath(mPath, mPaint)
        mPaint.color = Color.argb(OTHER_ALPHA, 244, 92, 71)
    }

    /**
     * 鱼尾及鱼尾张合
     *
     * @param canvas
     * @param mainPoint
     * @param length
     * @param maxWidth
     */
    private fun makeTail(
        canvas: Canvas,
        mainPoint: PointF,
        length: Float,
        maxWidth: Float,
        angle: Float
    ) {
        val newWidth =
            Math.abs(Math.sin(Math.toRadians(currentValue * 1.7 * waveFrequence)) * maxWidth + HEAD_RADIUS / 5 * 3)
                .toFloat()
        //endPoint为三角形底边中点
        val endPoint: PointF = calculatPoint(mainPoint, length, angle - 180)
        val endPoint2: PointF = calculatPoint(mainPoint, length - 10, angle - 180)
        val point1: PointF
        val point2: PointF
        val point3: PointF
        val point4: PointF
        point1 = calculatPoint(endPoint, newWidth, angle - 90)
        point2 = calculatPoint(endPoint, newWidth, angle + 90)
        point3 = calculatPoint(endPoint2, newWidth - 20, angle - 90)
        point4 = calculatPoint(endPoint2, newWidth - 20, angle + 90)
        //内
        mPath!!.reset()
        mPath.moveTo(mainPoint.x, mainPoint.y)
        mPath.lineTo(point3.x, point3.y)
        mPath.lineTo(point4.x, point4.y)
        mPath.lineTo(mainPoint.x, mainPoint.y)
        canvas.drawPath(mPath, mPaint)
        //外
        mPath.reset()
        mPath.moveTo(mainPoint.x, mainPoint.y)
        mPath.lineTo(point1.x, point1.y)
        mPath.lineTo(point2.x, point2.y)
        mPath.lineTo(mainPoint.x, mainPoint.y)
        canvas.drawPath(mPath, mPaint)
    }

    private fun setFinsAngle(currentValue: Float) {
        finsAngle = 45 * currentValue
    }


    fun setWaveFrequence(waveFrequence: Float) {
        this.waveFrequence = waveFrequence
    }

    @JvmName("getFinsAnimator1")
    private  fun getFinsAnimator(): ObjectAnimator? {
        return finsAnimator
    }

    open fun getMiddlePoint(): PointF? {
        return middlePoint
    }

    open fun setMiddlePoint(middlePoint: PointF) {
        this.middlePoint = middlePoint
    }


}