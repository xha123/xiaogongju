package com.example.demok.ui.dialog

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.baselib.base.BaseDialog
import com.example.demok.R
import com.example.demok.net.data.QstData
import com.example.demok.ui.adapter.BtmListAdapter
import com.example.demok.ui.adapter.QstBottomAdapter
import kotlinx.android.synthetic.main.layout_recycler_just.*


/**
 * @author: xha
 * @date: 2020/9/22 16:09
 * @Description:底部列表弹窗
 */
class BtmListDialog(context: Context, qstlist: MutableList<String>) : BaseDialog(context) {
    var mContext: Context? = null
    var qstList: MutableList<String> = arrayListOf()

    init {
        this.mContext = context
        this.qstList = qstlist
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCancelable(true)
        setCanceledOnTouchOutside(true)
        setContentView(R.layout.dialog_qstbottom)
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window!!.setGravity(Gravity.BOTTOM)
        window!!.setWindowAnimations(R.style.BottomDialogAnimation)
        setAdapter()
    }

    private fun setAdapter() {
        val btmListAdapter = BtmListAdapter()
        mRecyclerView.layoutManager = LinearLayoutManager(mContext)
        mRecyclerView.adapter = btmListAdapter
        btmListAdapter.setList(qstList)

        btmListAdapter.setOnItemClickListener { adapter, view, position ->
            if (onBottomClicker != null) {
                val strShow = adapter.getItem(position) as String
                onBottomClicker!!.onBottomClickListener(position, strShow)
                dismiss()
            }
        }
    }

    var onBottomClicker: OnBottomClicker? = null

    public interface OnBottomClicker {
        fun onBottomClickListener(position: Int, strShow: String)
    }

}