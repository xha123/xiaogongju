package com.example.demok.ui.adapter

import android.annotation.SuppressLint
import android.view.View
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.demok.R
import com.example.demok.net.data.HistoryData
import com.example.demok.net.data.QstData
import com.example.demok.utils.ImageUtils
import kotlinx.android.synthetic.main.item_history.view.*
import kotlinx.android.synthetic.main.item_qstlist.view.*


/**
 *考试题目列表适配器
 */
class QstListAdapter :
    BaseQuickAdapter<QstData, BaseViewHolder>(R.layout.item_qstlist) {

    companion object {
        //复选框
        const val UPDATE_CHECK = 1

        //解释
        const val UPDATE_EXP = 2
    }

    private val answerList = arrayOf(
        "A",
        "B",
        "C",
        "D",
        "AB",
        "AC",
        "AD",
        "BC",
        "BD",
        "CD",
        "ABC",
        "ABD",
        "ACD",
        "BCD",
        "ABCD"
    )

    private val answer1List = arrayOf(
        "正确",
        "错误"
    )

    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseViewHolder, item: QstData) {
        if (item.url.isNullOrEmpty()) {
            holder.itemView.ivShowQstlist.visibility = View.GONE
        } else {
            holder.itemView.ivShowQstlist.visibility = View.VISIBLE
            ImageUtils.loadUri(holder.itemView.ivShowQstlist, item.url)
        }

        holder.itemView.tvTitleQstlist.text = item.question
        //判断是否是选择题
        if (item.item3.isEmpty() || item.item4.isEmpty()) {
            item.isChooseQst = false
            holder.itemView.tvAnswer3.visibility = View.GONE
            holder.itemView.tvAnswer4.visibility = View.GONE
            if (item.item1.isEmpty()) {
                holder.itemView.tvAnswer1.text = "正确"
            } else {
                holder.itemView.tvAnswer1.text = item.item1
            }
            if (item.item2.isEmpty()) {
                holder.itemView.tvAnswer2.text = "错误"
            } else {
                holder.itemView.tvAnswer2.text = item.item2
            }
        } else {
            item.isChooseQst = true
            holder.itemView.tvAnswer3.visibility = View.VISIBLE
            holder.itemView.tvAnswer4.visibility = View.VISIBLE
            holder.itemView.tvAnswer1.text = "A." + item.item1
            holder.itemView.tvAnswer2.text = "B." + item.item2
            holder.itemView.tvAnswer3.text = "C." + item.item3
            holder.itemView.tvAnswer4.text = "D." + item.item4
        }

        holder.itemView.tvAnswer1.isSelected = item.isChoose1
        holder.itemView.tvAnswer2.isSelected = item.isChoose2
        holder.itemView.tvAnswer3.isSelected = item.isChoose3
        holder.itemView.tvAnswer4.isSelected = item.isChoose4

        when (item.qstType) {
            0 -> {
                holder.itemView.tvExplanQstlist.text = ""
                holder.itemView.tvReulstQstlist.text = ""
            }
            1 -> {
                holder.itemView.tvExplanQstlist.text = "解释：" + item.explains
                holder.itemView.tvReulstQstlist.text = "回答正确"
                holder.itemView.tvReulstQstlist.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.color_blue
                    )
                )
            }
            2 -> {
                holder.itemView.tvExplanQstlist.text = "解释：" + item.explains
                try {
                    var answerInt = item.answer.toInt()
                    answerInt -= 1
                    if (answerInt > 4) {
                        answerInt -= 2
                    }
                    if (item.item3.isEmpty() || item.item4.isEmpty()) {
                        holder.itemView.tvReulstQstlist.text =
                            "回答错误， 正确答案：" + answer1List[answerInt]
                    } else {
                        holder.itemView.tvReulstQstlist.text =
                            "回答错误， 正确答案：" + answerList[answerInt]
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    holder.itemView.tvReulstQstlist.text = "回答错误"
                }
                holder.itemView.tvReulstQstlist.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.color_red
                    )
                )
            }
        }

    }

    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseViewHolder, item: QstData, payloads: List<Any>) {
        super.convert(holder, item, payloads)
        if (payloads.isEmpty()) {
            if (item.url.isNullOrEmpty()) {
                holder.itemView.ivShowQstlist.visibility = View.GONE
            } else {
                holder.itemView.ivShowQstlist.visibility = View.VISIBLE
                ImageUtils.loadUri(holder.itemView.ivShowQstlist, item.url)
            }

            holder.itemView.tvTitleQstlist.text = item.question
            //判断是否是选择题
            if (item.item3.isEmpty() || item.item4.isEmpty()) {
                item.isChooseQst = false
                holder.itemView.tvAnswer3.visibility = View.GONE
                holder.itemView.tvAnswer4.visibility = View.GONE
                if (item.item1.isEmpty()) {
                    holder.itemView.tvAnswer1.text = "正确"
                } else {
                    holder.itemView.tvAnswer1.text = item.item1
                }
                if (item.item2.isEmpty()) {
                    holder.itemView.tvAnswer2.text = "错误"
                } else {
                    holder.itemView.tvAnswer2.text = item.item2
                }
            } else {
                item.isChooseQst = true
                holder.itemView.tvAnswer3.visibility = View.VISIBLE
                holder.itemView.tvAnswer4.visibility = View.VISIBLE
                holder.itemView.tvAnswer1.text = "A." + item.item1
                holder.itemView.tvAnswer2.text = "B." + item.item2
                holder.itemView.tvAnswer3.text = "C." + item.item3
                holder.itemView.tvAnswer4.text = "D." + item.item4
            }

            holder.itemView.tvAnswer1.isSelected = item.isChoose1
            holder.itemView.tvAnswer2.isSelected = item.isChoose2
            holder.itemView.tvAnswer3.isSelected = item.isChoose3
            holder.itemView.tvAnswer4.isSelected = item.isChoose4

            when (item.qstType) {
                0 -> {
                    holder.itemView.tvExplanQstlist.text = ""
                }
                1 -> {
                    holder.itemView.tvExplanQstlist.text = "解释：" + item.explains
                    holder.itemView.tvReulstQstlist.text = "回答正确"
                    holder.itemView.tvReulstQstlist.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.color_blue
                        )
                    )
                }
                2 -> {
                    holder.itemView.tvExplanQstlist.text = "解释：" + item.explains
                    try {
                        var answerInt = item.answer.toInt()
                        answerInt -= 1
                        if (answerInt > 4) {
                            answerInt -= 2
                        }
                        holder.itemView.tvReulstQstlist.text = "回答错误， 正确答案：" + answerList[answerInt]
                    } catch (e: Exception) {
                        e.printStackTrace()
                        holder.itemView.tvReulstQstlist.text = "回答错误"
                    }
                    holder.itemView.tvReulstQstlist.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.color_red
                        )
                    )
                }
            }

        } else {
            val type = payloads[0] as Int
            when (type) {
                UPDATE_CHECK -> {
                    holder.itemView.tvAnswer1.isSelected = item.isChoose1
                    holder.itemView.tvAnswer2.isSelected = item.isChoose2
                    holder.itemView.tvAnswer3.isSelected = item.isChoose3
                    holder.itemView.tvAnswer4.isSelected = item.isChoose4
                }
                UPDATE_EXP -> {
                    when (item.qstType) {
                        0 -> {
                            holder.itemView.tvExplanQstlist.text = ""
                        }
                        1 -> {
                            holder.itemView.tvExplanQstlist.text = "解释：" + item.explains
                            holder.itemView.tvReulstQstlist.text = "回答正确"
                            holder.itemView.tvReulstQstlist.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.color_blue
                                )
                            )
                        }
                        2 -> {
                            holder.itemView.tvExplanQstlist.text = "解释：" + item.explains
                            try {
                                var answerInt = item.answer.toInt()
                                answerInt -= 1
                                if (answerInt > 4) {
                                    answerInt -= 2
                                }
                                holder.itemView.tvReulstQstlist.text =
                                    "回答错误， 正确答案：" + answerList[answerInt]
                            } catch (e: Exception) {
                                e.printStackTrace()
                                holder.itemView.tvReulstQstlist.text = "回答错误"
                            }
                            holder.itemView.tvReulstQstlist.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.color_red
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}