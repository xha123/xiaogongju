package com.example.demok.ui.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.baselib.utils.setOnClickListenerWithTrigger
import com.example.demok.R
import com.example.demok.base.MyBaseActivity
import com.example.demok.view.MyPhotoView
import kotlinx.android.synthetic.main.activity_photoshow.*
import java.util.*

/**
 * @author: xha
 * @date: 2020/10/12 16:31
 * @Description:大图查看页面
 */
class PhotoActivity : MyBaseActivity() {

    var imgList = arrayListOf<String>()
    var pos = 0 //显示的下标

    companion object {
        @JvmStatic
        fun start(
            context: Activity,
            imgList: ArrayList<String>
        ) {
            val starter = Intent(context, PhotoActivity::class.java)
            val bundle = Bundle()
            bundle.putStringArrayList("img", imgList)
            starter.putExtras(bundle)
            context.startActivity(starter)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_photoshow
    }


    override fun initOnCreate() {
        showHeader(false)
        showTitle(false)

        imgList = intent.extras?.getStringArrayList("img") ?: arrayListOf()

        setViewPager()

    }

    override fun initOnClickerListener() {
        super.initOnClickerListener()
        tvLeftPhoto.setOnClickListenerWithTrigger {
            onBackPressed()
        }
    }

    private fun setViewPager() {
        photoshowViewpager.setAdapter(object : PagerAdapter() {
            override fun getCount(): Int {
                return imgList.size
            }

            override fun isViewFromObject(view: View, obj: Any): Boolean {
                return view == obj
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
//                return super.instantiateItem(container, position)

                val imageurl = imgList[position]
                val view: View = LayoutInflater.from(this@PhotoActivity)
                    .inflate(R.layout.photoshowview_item, null)
                val img: MyPhotoView =
                    view.findViewById<MyPhotoView>(R.id.photoshowMyphoto)
                img.tag = imageurl
                img.setImageUri(imageurl, 900, 900)

                container.addView(view) //把布局加到Viewpager的控件组里面
                return view //显示要显示的布局
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//                super.destroyItem(container, position, `object`)

                container.removeView(`object` as View)
            }
        })

        photoshowViewpager.currentItem = pos

        photoshowViewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            @SuppressLint("SetTextI18n")
            override fun onPageSelected(position: Int) {
                pos = position
                tvTitlePhoto.text = "查看图片 (" + (pos + 1) + "/" + imgList.size + ")"

            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })

    }

}