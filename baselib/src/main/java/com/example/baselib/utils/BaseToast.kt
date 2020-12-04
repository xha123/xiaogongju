package cn.newhope.librarycommon.extension

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast

/**
 * @author: xha
 * @date: 2020/9/4 10:34
 * @Description:吐司基类
 */

class BaseToast private constructor(context: Context) {
    private var mToast: Toast? = null
    private var mContext: Context = context

    /**
     * 建造者模式,设置Toast的各种属性
     */
    class Builder(context: Context?) {
        private var baseToast = context?.let { BaseToast(it) }

        /**
         * 建造者
         */
        fun build(): BaseToast? {
            return baseToast
        }
    }

    /**
     * 显示短时吐司
     *
     * @param text 文本
     */
    fun showShort(text: CharSequence) {
        show(text, Toast.LENGTH_SHORT)
    }

    /**
     * 显示长时吐司
     *
     * @param text 文本
     */
    fun showLong(text: CharSequence) {
        show(text, Toast.LENGTH_LONG)
    }

    /**
     * 显示吐司
     *
     * @param text     文本
     * @param duration 显示时长
     */
    @SuppressLint("ShowToast")
    private fun show(text: CharSequence, duration: Int) {
        cancel()
        if (text.isBlank()) return
        mToast = Toast.makeText(mContext, text, duration)
        mToast?.show()
    }


    /**
     * 取消吐司显示
     */
    private fun cancel() {
        mToast?.cancel()
        mToast = null
    }
}