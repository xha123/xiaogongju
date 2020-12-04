package com.example.demok.ui

import android.Manifest
import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.view.View
import com.example.baselib.base.HintDialog
import com.example.baselib.utils.LogUtils
import com.example.baselib.utils.SharedPreferencesUtils
import com.example.baselib.utils.setOnClickListenerWithTrigger
import com.example.baselib.utils.showToast
import com.example.demok.R
import com.example.demok.base.MyBaseActivity
import com.example.demok.ui.demo.DemoActivity
import com.example.demok.ui.home.*
import com.example.demok.utils.MyUtils
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : MyBaseActivity() {

    companion object {
        @JvmStatic
        fun start(context: Activity) {
            val starter = Intent(context, MainActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        moveTaskToBack(true)
    }

    var mdefault: ComponentName? = null
    var mdefault1: ComponentName? = null
    var mPm: PackageManager? = null
    override fun initOnCreate() {
        setLeftShow(false)
        setTitleText(R.string.app_name)
        setPermission()
        if (themeInt == 0) {
            tvThemeMain.setText("夜间模式")
        } else {
            tvThemeMain.setText("白天模式")
        }
        mdefault =
            ComponentName(baseContext, "com.example.demok.ui.other.LauncherActivity")
        mdefault1 = ComponentName(baseContext, "com.example.demok.activity_launcher")
        mPm = application.packageManager
        mPm?.setComponentEnabledSetting(
            mdefault1!!, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        );

        mPm?.setComponentEnabledSetting(
            mdefault!!,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    private fun setPermission() {
        val rxPermissions = RxPermissions(this)
            .request(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .subscribe { aBoolean ->
                if (aBoolean) {
                    LogUtils.i("获取权限成功")
                } else {
                    showHintDialog()
                }
            }
    }

    private fun showHintDialog() {
        val hintDialog = HintDialog(this)
        hintDialog.show()
        hintDialog.setHintText("请在应用权限页面开启相关权限")
        hintDialog.setLeftButton("退出应用", View.OnClickListener {
            hintDialog.dismiss()
            finish()
        })
        hintDialog.setRightButton("前往设置", View.OnClickListener {
            hintDialog.dismiss()
            MyUtils.gotoAppDetailIntent(this, 101)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            setPermission()
        }
    }

    override fun initOnClickerListener() {
        super.initOnClickerListener()
        textview.setOnClickListenerWithTrigger {
            mPm?.setComponentEnabledSetting(
                mdefault!!, PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            );

            mPm?.setComponentEnabledSetting(
                mdefault1!!,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )
            showToast("已生效")
        }
        tvWifiMain.setOnClickListenerWithTrigger {
            WifiActivity.start(this)
        }
        tvVideoMain.setOnClickListenerWithTrigger {
            VideoActivity.start(this)
        }
        tvThemeMain.setOnClickListenerWithTrigger {
            if (themeInt == 0) {
                themeInt = 1
                SharedPreferencesUtils.putShared("theme", themeInt)
                tvThemeMain.setText("白天模式")
            } else {
                themeInt = 0
                SharedPreferencesUtils.putShared("theme", themeInt)
                tvThemeMain.setText("夜间模式")
            }
            recreate()
        }

        tvHistoryMain.setOnClickListener {
            HistoryListActivity.start(this)
        }

        tvSmileMain.setOnClickListenerWithTrigger {
            SmileActivity.start(this)
        }

        tvOilPriceMain.setOnClickListenerWithTrigger {
            OilPriceActivity.start(this)
        }

        tvQstBankMain.setOnClickListenerWithTrigger {
            QstBankActivity.start(this)
        }



        tvBijiaMain.setOnClickListenerWithTrigger {
            showToast("功能开发中...")
//            PriceActivity.start(this)
        }

        tvMarqueeMain.setOnClickListenerWithTrigger {
            MarqueeActivity.start(this)
        }

        tvDemoMain.setOnClickListenerWithTrigger {
            DemoActivity.start(this)
        }

    }
}