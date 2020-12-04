package com.example.demok.ui.demo.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.demok.R
import kotlinx.android.synthetic.main.item_text.view.*

/**
 * @author: xha
 * @date: 2020/10/22 15:18
 * @Description:小功能
 */
class CusViewAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_text) {
    override fun convert(holder: BaseViewHolder, item: String) {
        holder.itemView.tvText.text = item
    }

}