package com.example.demok.ui.other

import android.annotation.SuppressLint
import com.example.demok.R
import com.example.demok.base.MyBaseActivity
import com.example.demok.ui.MainActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import java.util.concurrent.TimeUnit

/**
 * @author: xha
 * @date: 2020/10/13 14:52
 * @Description:
 */
class LauncherActivity : MyBaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_launcher
    }

    @SuppressLint("CheckResult")
    override fun initOnCreate() {
        showTitle(false)

        Observable.timer(2000,TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(Consumer {
                MainActivity.start(this)
            })
    }

}