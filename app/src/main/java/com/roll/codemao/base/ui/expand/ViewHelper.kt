package com.roll.www.zhulishitidian.utils.expand

import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.SpanUtils
import com.roll.codemao.utils.ResUtils
import java.util.ArrayList

/**
 * author： deemons
 * date:    2018/6/16
 * desc:    View 相关扩展
 */


/**
 * 防抖点击
 */
fun View.setOnClick(during: Long = 1000, listener: (view: View) -> Unit) {
    var lastClickTime = 0L
    this.setOnClickListener {
        if (System.currentTimeMillis() > lastClickTime + during) {
            lastClickTime = System.currentTimeMillis()
            listener(it)
        }
    }
}

fun TextView.setTextWithKey(text: String?, key: String?, keyWordColor: Int): CharSequence? {
    val create = getCharSequareWithKey(text, key, keyWordColor)
    setText(create)
    return create
}

fun getCharSequareWithKey(content: String?, key: String?, textColor: Int): CharSequence? {

    fun getIndex(startIndex: Int, content: String?, key: String?, list: MutableList<Int>): Int {
        val index = content!!.indexOf(key!!, startIndex, true)
        if (index >= 0) {
            list.add(index)
            return getIndex(index + key.length, content, key, list)
        }
        return index
    }

    if (TextUtils.isEmpty(key) || TextUtils.isEmpty(content)) return null
    val indexArrayList = ArrayList<Int>()
    getIndex(0, content, key, indexArrayList)
    if (indexArrayList.size == 0) return content
    val spUtils = SpanUtils()
    var index = 0
    indexArrayList.forEach {
        spUtils.append(content!!.substring(index, it))
                .append(content.substring(it, it + key!!.length)).setForegroundColor(ResUtils.getColor(textColor))
        index = it + key.length
    }
    spUtils.append(content!!.substring(index, content.length))
    return spUtils.create()
}


