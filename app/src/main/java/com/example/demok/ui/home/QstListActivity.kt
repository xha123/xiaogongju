package com.example.demok.ui.home

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.baselib.base.HintDialog
import com.example.baselib.utils.showToast
import com.example.demok.R
import com.example.demok.base.MyBaseActivity
import com.example.demok.net.DataManager
import com.example.demok.net.MyObserver
import com.example.demok.net.ResponseCallBack
import com.example.demok.net.data.QstData
import com.example.demok.net.data.ResultData
import com.example.demok.ui.adapter.QstListAdapter
import com.example.demok.ui.dialog.QstBottomDialog
import com.example.demok.utils.Const.JUHE_QST_BANK_KEY
import com.example.demok.utils.MyUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_qstlist.*

/**
 * @author: xha
 * @date: 2020/10/10 16:04
 * @Description: 考试列表页面
 */
class QstListActivity : MyBaseActivity() {

    private var title = ""

    //是否取消考试
    private var isCancel = false
    private var subject = ""
    private var model = ""
    private var numTopic = 100

    private lateinit var layoutManager: LinearLayoutManager
    private var startTime: Long = 0

    private val qstListAdapter by lazy {
        QstListAdapter()
    }

    companion object {
        @JvmStatic
        fun start(
            context: Activity,
            title: String,
            subject: String,
            model: String,
            numTopic: Int
        ) {
            val starter = Intent(context, QstListActivity::class.java)
                .putExtra("title", title)
                .putExtra("subject", subject)
                .putExtra("model", model)
                .putExtra("numTopic", numTopic)
            context.startActivity(starter)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_qstlist
    }


    override fun onBackPressed() {
        if (isCancel) {
            super.onBackPressed()
        } else {
            showHintDialog()
        }

    }

    private fun showHintDialog() {
        val hintDialog = HintDialog(this)
        hintDialog.show()
        hintDialog.setHintText("确定要取消考试吗？")
        hintDialog.setRightClickListener(View.OnClickListener {
            isCancel = true
            hintDialog.dismiss()
            onBackPressed()
        })
    }


    override fun initOnCreate() {
        title = intent.getStringExtra("title") ?: ""
        subject = intent.getStringExtra("subject") ?: ""
        model = intent.getStringExtra("model") ?: ""
        numTopic = intent.getIntExtra("numTopic", 100)

        setTitleText(title)

        if (themeInt == 0) {
            setRightImg(R.drawable.more)
        } else {
            setRightImg(R.drawable.more_white)
        }

        setRightButton(View.OnClickListener {
            showBottomDialog()
        })

        setAdapter()

        getData()

        startTime = System.currentTimeMillis()
    }

    private fun showBottomDialog() {
        val qstBottomDialog = QstBottomDialog(this, qstListAdapter.data)
        qstBottomDialog.show()
        qstBottomDialog.onBottomClicker = object : QstBottomDialog.OnBottomClicker {
            override fun onBottomClickListener(position: Int) {
                smoothMoveToPosition(mRecyclerViewQstlist, position)
//                layoutManager.scrollToPosition(position - 1)
            }
        }
    }

    //目标项是否在最后一个可见项之后
    private var mShouldScroll = false

    //记录目标项位置
    private var mToPosition = 0

    //目标项是否在最后一个可见项之后 private boolean mShouldScroll; //记录目标项位置 private int mToPosition;
    //滑动到指定位置
    private fun smoothMoveToPosition(mRecyclerView: RecyclerView, position: Int) { // 第一个可见位置
        val firstItem: Int = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0))
        // 最后一个可见位置
        val lastItem: Int =
            mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.childCount - 1))
        if (position < firstItem) {
            // 第一种可能:跳转位置在第一个可见位置之前
            mRecyclerView.scrollToPosition(position)
        } else if (position <= lastItem) {
            // 第二种可能:跳转位置在第一个可见位置之后
            val movePosition = position - firstItem
            if (movePosition >= 0 && movePosition < mRecyclerView.childCount) {
                val top: Int = mRecyclerView.getChildAt(movePosition).getTop()
                mRecyclerView.smoothScrollBy(0, top)
            }
        } else {
            // 第三种可能:跳转位置在最后可见项之后
            mRecyclerView.scrollToPosition(position)
            mToPosition = position
            mShouldScroll = true
        }
    }

    private fun getData() {
        showProDialog()
        DataManager.getInstances().apiService.qstBankList(
            JUHE_QST_BANK_KEY,
            subject, model, "rand"
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(MyObserver(object : ResponseCallBack<ResultData<List<QstData>>> {
                override fun onSuccess(t: ResultData<List<QstData>>) {
                    dismissProDialog()

                    qstListAdapter.setList(t.result.subList(0, numTopic))
                }

                override fun onFail(code: String, errorMsg: String) {
                    dismissProDialog()
                }
            }))
    }

    private fun setAdapter() {
        layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        mRecyclerViewQstlist.layoutManager = layoutManager
        mRecyclerViewQstlist.adapter = qstListAdapter

        val viewEmpty = View.inflate(this, R.layout.layout_nodata, null)
        qstListAdapter.setEmptyView(viewEmpty)

        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(mRecyclerViewQstlist)

        qstListAdapter.addChildClickViewIds(
            R.id.tvAnswer1,
            R.id.tvAnswer2,
            R.id.tvAnswer3,
            R.id.tvAnswer4,
            R.id.tvLeftQstlist,
            R.id.tvRightQstlist,
            R.id.tvSubmitQstlist,
            R.id.ivShowQstlist
        )
        qstListAdapter.setOnItemChildClickListener { adapter, view, position ->
            val qstData = adapter.getItem(position) as QstData
            when (view.id) {
                R.id.ivShowQstlist -> {
                    val imgList = arrayListOf<String>()
                    imgList.add(qstData.url)
                    PhotoActivity.start(this, imgList)
                }
                R.id.tvAnswer1 -> {
                    //已回答不作修改
                    if (qstData.qstType != 0) {
                        return@setOnItemChildClickListener
                    }
                    if (qstData.isChooseQst) {
                        qstData.isChoose1 = !qstData.isChoose1
                    } else {
                        qstData.isChoose1 = !qstData.isChoose1
                        qstData.isChoose2 = false
                    }
                    updateCheck(qstData, position)
                }
                R.id.tvAnswer2 -> {
                    //已回答不作修改
                    if (qstData.qstType != 0) {
                        return@setOnItemChildClickListener
                    }
                    if (qstData.isChooseQst) {
                        qstData.isChoose2 = !qstData.isChoose2
                    } else {
                        qstData.isChoose1 = false
                        qstData.isChoose2 = !qstData.isChoose2
                    }
                    updateCheck(qstData, position)
                }
                R.id.tvAnswer3 -> {
                    //已回答不作修改
                    if (qstData.qstType != 0) {
                        return@setOnItemChildClickListener
                    }
                    if (!qstData.isChooseQst) {
                        return@setOnItemChildClickListener
                    }
                    qstData.isChoose3 = !qstData.isChoose3
                    updateCheck(qstData, position)
                }
                R.id.tvAnswer4 -> {
                    //已回答不作修改
                    if (qstData.qstType != 0) {
                        return@setOnItemChildClickListener
                    }
                    if (!qstData.isChooseQst) {
                        return@setOnItemChildClickListener
                    }
                    qstData.isChoose4 = !qstData.isChoose4
                    updateCheck(qstData, position)
                }
                R.id.tvLeftQstlist -> {
                    if (position > 0) {
//                        layoutManager.scrollToPosition(position - 1)
                        smoothMoveToPosition(mRecyclerViewQstlist, position - 1)
                    } else {
                        showToast("没有啦")
                    }
                }
                R.id.tvRightQstlist -> {
                    if (qstData.qstType == 0) {
                        val changeData = setReulst(qstData, position)
                        //回答错误停留在当前item
                        if (changeData.qstType == 2) {
                            return@setOnItemChildClickListener
                        } else if (changeData.qstType == 0) {
                            showToast("该题未作答")
                        }
                    }
                    if (position == qstListAdapter.data.size - 1) {
                        subMitQst()
                        return@setOnItemChildClickListener
                    }
                    smoothMoveToPosition(mRecyclerViewQstlist, position + 1)
//                    layoutManager.scrollToPosition(position + 1)
                }
                R.id.tvSubmitQstlist -> {
                    subMitQst()
                }
            }
//            qstListAdapter.setData(position, qstData)

        }
    }

    private fun subMitQst() {
        val untreated = qstListAdapter.data.count {
            it.qstType == 0
        }
        var scoreInt = qstListAdapter.data.count {
            it.qstType == 1
        }
        if (numTopic == 20) {
            scoreInt *= 5
        } else if (numTopic == 50) {
            scoreInt *= 2
        }
        showSubmitDialog(untreated, scoreInt)
    }

    //判断是否作答改题
    private fun setReulst(qstData: QstData, position: Int): QstData {
        var resultStr: String = ""
        resultStr = if (qstData.isChoose1) {
            if (qstData.isChoose2) {
                if (qstData.isChoose3) {
                    if (qstData.isChoose4) {
                        "17"
                    } else {
                        "13"
                    }
                } else if (qstData.isChoose4) {
                    "14"
                } else {
                    "7"
                }
            } else if (qstData.isChoose3) {
                if (qstData.isChoose4) {
                    "15"
                } else {
                    "8"
                }
            } else if (qstData.isChoose4) {
                "9"
            } else {
                "1"
            }
        } else if (qstData.isChoose2) {
            if (qstData.isChoose3) {
                if (qstData.isChoose4) {
                    "16"
                } else {
                    "10"
                }
            } else if (qstData.isChoose4) {
                "11"
            } else {
                "2"
            }
        } else if (qstData.isChoose3) {
            if (qstData.isChoose4) {
                "12"
            } else {
                "3"
            }
        } else if (qstData.isChoose4) {
            "4"
        } else {
            qstData.qstType = 0
            "0"
            return qstData
        }
        if (resultStr == qstData.answer) {
            qstData.qstType = 1
            showToast("回答正确")
        } else {
            qstData.qstType = 2
            showToast("回答错误")
        }
        qstListAdapter.data[position] = qstData
        qstListAdapter.notifyItemChanged(position, QstListAdapter.UPDATE_EXP)
        return qstData
    }

    private fun updateCheck(qstData: QstData, position: Int) {
        //brvah不建议直接修改数据源，可继承后自定义方法修改
        qstListAdapter.data[position] = qstData
        qstListAdapter.notifyItemChanged(position, QstListAdapter.UPDATE_CHECK)
    }

    private fun showSubmitDialog(untreated: Int, scoreInt: Int) {
        val hintDialog = HintDialog(this)
        hintDialog.show()
        if (untreated == 0) {
            hintDialog.setHintText("确定要交卷吗？")
        } else {
            hintDialog.setHintText("确定要交卷吗？\n尚有" + untreated + "道题目未完成！")
        }
        hintDialog.setRightClickListener(View.OnClickListener {
            hintDialog.dismiss()
            val timeShow =
                MyUtils.showTime(System.currentTimeMillis() - startTime, "mm:ss")
            QstReulstActivity.start(this, title, subject, model, scoreInt, numTopic, timeShow)
            finish()
        })
    }

}