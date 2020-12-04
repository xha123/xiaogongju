package com.example.baselib.base

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle

import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.baselib.R


/**
 * @author: xha
 * @date: 2020/9/29 15:56
 * @Description:
 */

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //        ArmsUtils.statuInScreen(this)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        getLayoutId()?.let {
            setContentView(it)
        }

        initOnCreate()

        initOnClickerListener()
    }

    abstract fun getLayoutId(): Int?

    abstract fun initOnCreate()

    override fun onDestroy() {
        super.onDestroy()
        dismissProDialog()
    }

    /**
     * 初始化点击事件
     */
    protected open fun initOnClickerListener() {

    }

    /**
     * 设置导航栏字体颜色  黑色 true / 白色 false
     *
     * @param isBlack
     */
    fun setBlcakShow(isBlack: Boolean = true) {
        if (isBlack) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //设置状态栏文字颜色及图标为深色
                this.window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                window.navigationBarColor = ContextCompat.getColor(this, R.color.color_white)
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //设置状态栏文字颜色及图标为浅色
                window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                window.navigationBarColor = ContextCompat.getColor(this, R.color.black)
            }
        }
    }


    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    protected open fun getStatusBarHeight(context: Context): Int {
        val resources = context.resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }


    //设置字体不随系统改变
    override fun onConfigurationChanged(newConfig: Configuration) {
        if (newConfig.fontScale != 1f) //非默认值
            resources
        super.onConfigurationChanged(newConfig)
    }

    override fun getResources(): Resources? {
        val res = super.getResources()
        if (res.configuration.fontScale != 1f) { //非默认值
            val newConfig = Configuration()
            newConfig.setToDefaults() //设置默认
            res.updateConfiguration(newConfig, res.displayMetrics)
        }
        return res
    }

    var progressDialog: ProgressDialog? = null
//    fun showDialog() {
//        if (progressDialog == null) {
//            progressDialog = ProgressDialog(this)
//        }
//        progressDialog!!.show()
//    }

    fun showProDialog(hintStr: String? = "加载中...") {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(this)
        }
        progressDialog?.let {
            it.show()
            it.setProText(hintStr!!)
        }
    }

    fun dismissProDialog() {
        progressDialog?.let {
            if (progressDialog!!.isShowing) {
                progressDialog?.dismiss()
            }
        }
    }

}