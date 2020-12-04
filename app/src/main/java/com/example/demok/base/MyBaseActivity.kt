package com.example.demok.base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.example.baselib.base.BaseActivity
import com.example.baselib.utils.LogUtils
import com.example.baselib.utils.SharedPreferencesUtils
import com.example.demok.R
import kotlinx.android.synthetic.main.app_title.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

/**
 * @author: xha
 * @date: 2020/9/30 16:36
 * @Description:
 */
abstract class MyBaseActivity : BaseActivity(), CoroutineScope by MainScope() {

    var themeInt = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        themeInt = SharedPreferencesUtils.getShared("theme", 0) as Int
        LogUtils.i("theme $themeInt")
        //0是白天模式  1黑夜模式
        if (themeInt == 0) {
            setTheme(R.style.AppTheme)
            setBlcakShow(true)
        } else if (themeInt == 1) {
            setTheme(R.style.blackTheme)
            setBlcakShow(false)
        }
        super.onCreate(savedInstanceState)
    }


    @SuppressLint("ResourceType")
    override fun setContentView(@LayoutRes layoutResID: Int) {
        super.setContentView(R.layout.app_title)
        initHeader()
        if (layoutResID > 0) {
            LayoutInflater.from(this).inflate(layoutResID, title_layout, true)
        }
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        initHeader()
        if (view != null) {
            title_layout!!.addView(view)
        }
    }

    private fun initHeader() {
        //适配状态栏高度
        val linearParams: LinearLayout.LayoutParams =
            view_header_title.layoutParams as LinearLayout.LayoutParams
        linearParams.height = getStatusBarHeight(this)

        setBackClickerListener { onBackPressed() }
    }

    /**
     * 是否显示title
     *
     * @param hasTitle
     */
    open fun showTitle(hasTitle: Boolean) {
        rl_title!!.visibility = if (hasTitle) View.VISIBLE else View.GONE
    }

    /**
     * 是否显示状态栏
     *
     * @param hasHeader 是否显示头部
     */
    open fun showHeader(hasHeader: Boolean) {
        view_header_title.visibility = if (hasHeader) View.VISIBLE else View.GONE
    }

    /**
     * 设置主体背景
     */
    open fun setHeaderColor(drawableRes: Int) {
        setTitleBackGround(drawableRes)
        setHeaderBackGround(drawableRes)
    }

    /**
     * 设置title背景色
     *
     * @param drawableRes
     */
    open fun setTitleBackGround(drawableRes: Int) {
        rl_title.setBackgroundColor(ContextCompat.getColor(this, drawableRes))
    }

    /**
     * 设置导航栏颜色
     */
    open fun setHeaderBackGround(drawableRes: Int) {
        view_header_title.setBackgroundColor(ContextCompat.getColor(this, drawableRes))
    }

    /**
     * 设置返回按钮点击事件
     */
    fun setBackClickerListener(onClickListener: View.OnClickListener) {
        tv_left_title.setOnClickListener(onClickListener)
    }


    /**
     * 设置header背景色
     *
     * @param drawableRes
     */
    open fun showHeaderBackGround(drawableRes: Int) {
        view_header_title.setBackgroundColor(ContextCompat.getColor(this, drawableRes))
    }

    /**
     * 设置title文字
     *
     * @param titleId
     */
    open fun setTitleText(titleId: Int) {
        tv_title.setText(titleId)
    }

    /**
     * 设置title文字
     *
     * @param title
     */
    open fun setTitleText(title: String?) {
        tv_title.text = title
    }

    //设置title点击事件
    open fun setTitleOnClicker(onClicker: View.OnClickListener?) {
        tv_title.setOnClickListener(onClicker)
    }

    /**
     * 设置左边按钮文字
     *
     * @param titleId
     */
    fun setLeftText(titleId: Int) {
        tv_left_title.setText(titleId)
    }

    /**
     * 设置返回键是否显示
     */
    fun setLeftShow(isShow: Boolean) {
        tv_left_title.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    /**
     * 设置左边按钮文字
     *
     * @param title
     */
    open fun setLeftText(title: String?) {
        tv_left_title.setText(title)
    }

    /**
     * 设置右边按钮文字
     *
     * @param titleId
     */
    open fun setRightText(titleId: Int) {
        tv_right_title.setText(titleId)
    }

    /**
     * 设置右边按钮文字
     */
    open fun setRightText(title: String?) {
        tv_right_title.setText(title)
    }

    /**
     * 设置右边按钮图片
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    open fun setRightImg(resId: Int) {
        val drawable = ContextCompat.getDrawable(this, resId)
        tv_right_title.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
    }

    /**
     * 设置右边按钮颜色
     */
    open fun setRightTextColor(colorId: Int) {
        tv_right_title.setTextColor(ContextCompat.getColor(this, colorId))
    }

    /**
     * 设置右边是否可以点击
     */
    open fun setRightClickable(clickable: Boolean) {
        tv_right_title.isEnabled = clickable
    }

    /**
     * 设置右边点击事件
     */
    open fun setRightButton(onClickListener: View.OnClickListener?) {
        tv_right_title.setOnClickListener(onClickListener)
    }


    /**
     * 收起软键盘
     */
    open fun hideKeyboard(view: View) {
        val imm = view.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}