package com.example.demok.net.data

import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * @author: xha
 * @date: 2020/9/28 14:49
 * @Description:
 */
data class ResultData<T>(
    val code: String?,
    val message: String? = "",
    val body: T,
    val error_code: Int,
    val result: T
)

data class InfoModel(
    var path: String,
    var version: String,
    var size: String,
    var name: String,
    var packageName: String,
    var installed: Boolean,
    var icon: Drawable?
)

/**
 * 	content	string	笑话内容或题目
 *  hashId	string	笑话的唯一标识
 * unixtime	long	笑话更新的时间戳
 */
data class SmileData(
    val content: String,
    val hashId: String,
    val unixtime: Long
)

/**
 * 驾照考试
 *  "id": 12,
"question": "这个标志是何含义？",//问题
"answer": "4",//答案
"item1": "前方40米减速",//选项，当内容为空时表示判断题正确选项
"item2": "最低时速40公里",//选项，当内容为空时表示判断题错误选项
"item3": "限制40吨轴重",
"item4": "限制最高时速40公里",
"explains": "限制最高时速40公里：表示该标志至前方限制速度标志的路段内，机动车行驶速度不得超过标志所示数值。此标志设在需要限制车辆速度的路段的起点。以图为例：限制行驶时速不得超过40公里。",//答案解释
"url": "http://images.juheapi.com/jztk/c1c2subject1/12.jpg"//图片url
 */
data class QstData(
    val id: String,
    val question: String,
    val answer: String,
    val item1: String,
    val item2: String,
    val item3: String,
    val item4: String,
    val explains: String,
    val url: String
) {
    //是否是选择题
    var isChooseQst: Boolean = true
    var isChoose1: Boolean = false
    var isChoose2: Boolean = false
    var isChoose3: Boolean = false
    var isChoose4: Boolean = false

    //问题状态 0未处理 1正确 2错误
    var qstType: Int = 0
}

/**
 * 油价
 */
data class OilPriceData(
    val city: String,
    @SerializedName("92h")
    val show92: String,
    @SerializedName("95h")
    val show95: String,
    @SerializedName("98h")
    val show98: String,
    @SerializedName("0h")
    val show0: String
)

/**
 * @param pic 图片
 */
data class HistoryData(
    val day: String,
    val month: String,
    val year: String,
    val title: String,
    var pic: String,
    val lunar: String,
    val des: String,
    @SerializedName("id", alternate = ["_id"])
    val id: String,
    var content: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(day)
        dest?.writeString(month)
        dest?.writeString(year)
        dest?.writeString(title)
        dest?.writeString(pic)
        dest?.writeString(lunar)
        dest?.writeString(des)
        dest?.writeString(id)
        dest?.writeString(content)
    }

    companion object CREATOR : Parcelable.Creator<HistoryData> {
        override fun createFromParcel(parcel: Parcel): HistoryData {
            return HistoryData(parcel)
        }

        override fun newArray(size: Int): Array<HistoryData?> {
            return arrayOfNulls(size)
        }
    }

}