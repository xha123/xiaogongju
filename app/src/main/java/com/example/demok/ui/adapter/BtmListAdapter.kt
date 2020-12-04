package com.example.demok.ui.adapter

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.demok.R
import com.example.demok.net.data.HistoryData
import com.example.demok.net.data.QstData
import com.example.demok.utils.ImageUtils
import kotlinx.android.synthetic.main.dialog_btmlist.view.*
import kotlinx.android.synthetic.main.item_history.view.*
import kotlinx.android.synthetic.main.item_qstbottom.view.*


/**
 *底部列表适配器
 */
class BtmListAdapter :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.dialog_btmlist) {

    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseViewHolder, item: String) {
        holder.itemView.tvShowBtmlist.text = item
    }
}