package com.example.demok.ui.demo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.baselib.utils.setOnClickListenerWithTrigger
import com.example.demok.R
import com.example.demok.base.MyBaseActivity
import com.example.demok.databinding.ActivityDatabindBinding
import com.example.demok.ui.demo.model.DataOne
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_databind.*
import javax.inject.Inject

/**
 * @author: xha
 * @date: 2020/12/4 14:12
 * @Description:
 */
@AndroidEntryPoint
class DataBindActivity : AppCompatActivity() {

    @Inject
    lateinit var dataOne: DataOne

    companion object {
        @JvmStatic
        fun start(
            context: Activity
        ) {
            val starter = Intent(context, DataBindActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityDatabindBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_databind)
//        dataOne = DataOne("老大哥", "点击")
        dataOne.dataName = "以前我不知道我是谁"
        dataOne.dataRemark = "现在呢？"
        binding.dataOne = dataOne

        tvChange.setOnClickListenerWithTrigger {
            dataOne.dataName = "现在我想做一个好人"
            binding.dataOne = dataOne
        }
    }

}