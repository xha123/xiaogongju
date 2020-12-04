package com.example.demok.ui.adapter

import android.annotation.SuppressLint
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.demok.R
import com.example.demok.net.data.InfoModel
import kotlinx.android.synthetic.main.layout_book_item.view.*


/**
 *选择整改单位适配器
 */
class WifiAdapter :
    BaseQuickAdapter<InfoModel, BaseViewHolder>(R.layout.layout_book_item) {

    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseViewHolder, infoModel: InfoModel) {
        holder.itemView.tv_name.text = infoModel.name + "(v" + infoModel.version + ")"

        holder.itemView.tv_size.text = infoModel.size
        holder.itemView.tv_path.text = infoModel.path
        holder.itemView.iv_icon.setImageDrawable(infoModel.icon)
        if (infoModel.installed) {
            holder.itemView.tv_delete.visibility = View.VISIBLE
        } else {
            holder.itemView.tv_delete.visibility = View.GONE
        }
    }
}