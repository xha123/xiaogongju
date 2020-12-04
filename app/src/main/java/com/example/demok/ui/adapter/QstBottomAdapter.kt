package com.example.demok.ui.adapter

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.demok.R
import com.example.demok.net.data.HistoryData
import com.example.demok.net.data.QstData
import com.example.demok.utils.ImageUtils
import kotlinx.android.synthetic.main.item_history.view.*
import kotlinx.android.synthetic.main.item_qstbottom.view.*


/**
 *选择整改单位适配器
 */
class QstBottomAdapter :
    BaseQuickAdapter<QstData, BaseViewHolder>(R.layout.item_qstbottom) {

    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseViewHolder, item: QstData) {

        holder.itemView.tvShowQstBootom.text = "第" + (holder.bindingAdapterPosition+1) + "题"

        if (item.qstType == 0) {
            holder.itemView.tvShowQstBootom.setBackgroundResource(R.drawable.shape_r5_dadada)
            holder.itemView.tvShowQstBootom.setTextColor(ContextCompat.getColor(context,R.color.color_333333))
        } else if (item.qstType == 1) {
            holder.itemView.tvShowQstBootom.setBackgroundResource(R.drawable.shape_r5_blue)
            holder.itemView.tvShowQstBootom.setTextColor(ContextCompat.getColor(context,R.color.white))
        } else if (item.qstType == 2) {
            holder.itemView.tvShowQstBootom.setBackgroundResource(R.drawable.shape_r5_red)
            holder.itemView.tvShowQstBootom.setTextColor(ContextCompat.getColor(context,R.color.white))
        }
    }
}