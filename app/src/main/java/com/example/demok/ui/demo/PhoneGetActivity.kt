package com.example.demok.ui.demo

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.telephony.TelephonyManager
import androidx.core.app.ActivityCompat
import com.example.baselib.utils.LogUtils
import com.example.baselib.utils.setOnClickListenerWithTrigger
import com.example.baselib.utils.showToast
import com.example.demok.R
import com.example.demok.base.MyBaseActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_phone_get.*


/**
 * @author: xha
 * @date: 2020/11/13 11:03
 * @Description:
 */
class PhoneGetActivity : MyBaseActivity() {

    companion object {
        @JvmStatic
        fun start(
            context: Activity
        ) {
            val starter = Intent(context, PhoneGetActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun getLayoutId(): Int? {
        return R.layout.activity_phone_get
    }

    override fun initOnCreate() {
        setTitleText("获取手机信息")
        setPermission()
    }

    private fun setPermission() {
        val rxPermissions = RxPermissions(this)
            .request(
                Manifest.permission.READ_PHONE_STATE,
            )
            .subscribe { aBoolean ->
                if (aBoolean) {
                    LogUtils.i("获取手机信息权限成功")
                } else {
                    showToast("获取手机信息权限失败")
                }
            }
    }

    override fun initOnClickerListener() {
        super.initOnClickerListener()
        tvGetPhone.setOnClickListenerWithTrigger {
            val phone = getPhoneNumber()
            if (phone.isEmpty()) {
                tvPhone.text = "获取手机号失败"
            } else {
                tvPhone.text = phone
            }
        }
    }

    @SuppressLint("HardwareIds")
    private fun getPhoneNumber(): String {
        val tm = this.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        var phone = ""
        val te1 = if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_SMS
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_NUMBERS
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            phone = tm.line1Number ?: "" //获取本机号码
            if (phone.isEmpty()) {
                getOtherPhone(phone)
            }
            return phone
        } else {
            LogUtils.e("获取权限失败")
            return phone
        }
    }

    private fun getOtherPhone(phone: String): String {

        return phone
    }


}