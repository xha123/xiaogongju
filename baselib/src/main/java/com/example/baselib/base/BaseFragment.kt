package com.example.baselib.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

/**
 *@创建者wwy
 *@创建时间 2020/10/9 11:10
 *@描述
 */
abstract class BaseFragment : Fragment() {
    private var isLoaded = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(setLayoutResId(), container, false)
    }

    override fun onResume() {
        super.onResume()
        if (!isLoaded && !isHidden) {
            onFragmentFirstVisible()
            isLoaded = true
        }
    }

    protected open fun onFragmentFirstVisible() {
        initView()
        initData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isLoaded = false
        dismissProDialog()
    }

    abstract fun setLayoutResId(): Int

    abstract fun initView()

    abstract fun initData()


    var progressDialog: ProgressDialog? = null


    fun showProDialog(hintStr: String? = "加载中...") {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(requireContext())
        }
        progressDialog?.let {
            it.show()
            it.setProText(hintStr!!)
        }
    }

    fun dismissProDialog() {
        progressDialog?.let {
            if (progressDialog!!.isShowing) {
                progressDialog?.dismiss()
            }
        }
    }

}