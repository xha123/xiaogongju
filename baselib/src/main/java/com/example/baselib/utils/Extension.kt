package com.example.baselib.utils

import android.app.Application
import android.content.Intent
import android.content.SharedPreferences
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import cn.newhope.librarycommon.extension.BaseToast
import com.example.baselib.R
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


/**
 * @author wq
 * @date 2019-04-28 17:19
 * @desc ${TODO}
 */

fun Fragment.toast(msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.toast(msg: String) {
    Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
}

fun FragmentActivity.toast(msg: String) {
    Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
}


fun Fragment.showToast(msg: String) {
    BaseToast.Builder(context).build()?.showShort(msg)
}

fun FragmentActivity.showToast(msg: String) {
    BaseToast.Builder(applicationContext).build()?.showShort(msg)
}

fun Application.showToast(msg: String) {
    BaseToast.Builder(applicationContext).build()?.showShort(msg)
}


fun Fragment.startActivity(cls: Class<*>) {
    val intent = Intent(context, cls)
    startActivity(intent)
}

fun AppCompatActivity.startActivity(cls: Class<*>) {
    val intent = Intent(this, cls)
    startActivity(intent)
}


private var <T : View>T.triggerLastTime: Long
    get() = if (getTag(R.id.common_triggerLastTimeKey) != null) getTag(R.id.common_triggerLastTimeKey) as Long else 0
    set(value) {
        setTag(R.id.common_triggerLastTimeKey, value)
    }

private var <T : View> T.triggerDelay: Long
    get() = if (getTag(R.id.common_triggerDelayKey) != null) getTag(R.id.common_triggerDelayKey) as Long else -1
    set(value) {
        setTag(R.id.common_triggerDelayKey, value)
    }

private fun <T : View> T.clickEnable(): Boolean {
    var clickable = false
    val currentClickTime = System.currentTimeMillis()
    if (currentClickTime - triggerLastTime >= triggerDelay) {
        clickable = true
    }
    triggerLastTime = currentClickTime
    return clickable
}

fun <T : View> T.setOnClickListenerWithTrigger(delay: Long = 800, block: (T) -> Unit) {
    triggerDelay = delay
    setOnClickListener {
        if (clickEnable()) {
            block(this)
        }
    }
}

fun <T : View> T.setOnClickListenerWithTrigger(listener: View.OnClickListener,delay: Long = 800) {
    triggerDelay = delay
    if (clickEnable()) {
        setOnClickListener(listener)
    }
}


private inline fun <T> SharedPreferences.delegate(
    key: String? = null,
    defaultValue: T,
    crossinline getter: SharedPreferences.(String, T) -> T,
    crossinline setter: SharedPreferences.Editor.(String, T) -> SharedPreferences.Editor
): ReadWriteProperty<Any, T> =
    object : ReadWriteProperty<Any, T> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T =
            getter(key ?: property.name, defaultValue)!!

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) =
            edit().setter(key ?: property.name, value).apply()
    }

fun SharedPreferences.int(key: String? = null, defValue: Int = 0): ReadWriteProperty<Any, Int> {

    return delegate(key, defValue, SharedPreferences::getInt, SharedPreferences.Editor::putInt)
}

fun SharedPreferences.long(
    key: String? = null,
    defValue: Long = 0
): ReadWriteProperty<Any, Long> {
    return delegate(key, defValue, SharedPreferences::getLong, SharedPreferences.Editor::putLong)
}

fun SharedPreferences.float(
    key: String? = null,
    defValue: Float = 0f
): ReadWriteProperty<Any, Float> {
    return delegate(key, defValue, SharedPreferences::getFloat, SharedPreferences.Editor::putFloat)
}

fun SharedPreferences.boolean(
    key: String? = null,
    defValue: Boolean = false
): ReadWriteProperty<Any, Boolean> {
    return delegate(
        key,
        defValue,
        SharedPreferences::getBoolean,
        SharedPreferences.Editor::putBoolean
    )
}

fun SharedPreferences.string(
    key: String? = null,
    defValue: String = ""
): ReadWriteProperty<Any, String?> {

    return delegate(
        key,
        defValue,
        SharedPreferences::getString,
        SharedPreferences.Editor::putString
    )
}




