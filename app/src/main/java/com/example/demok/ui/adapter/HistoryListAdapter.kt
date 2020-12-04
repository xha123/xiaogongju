package com.example.demok.ui.adapter

import android.annotation.SuppressLint
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.demok.R
import com.example.demok.net.data.HistoryData
import com.example.demok.utils.ImageUtils
import kotlinx.android.synthetic.main.item_history.view.*


/**
 *选择整改单位适配器
 */
class HistoryListAdapter :
    BaseQuickAdapter<HistoryData, BaseViewHolder>(R.layout.item_history) {

    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseViewHolder, item: HistoryData) {

        holder.itemView.tvTitleHistory.text = item.title
        holder.itemView.tvdetailHistory.text = item.des

        ImageUtils.loadUri(holder.itemView.ivShowHistory, item.pic)
    }
}