package com.roll.codemao.base.ui.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle

object PageJumpHelper {

    /**
     * 页面跳转
     * 我希望所有的页面都能用这种方式进行页面跳转
     */
    fun jumpActivity(context: Context, activityClass: Class<*>?,
             bundle: Bundle? = null, finish: Boolean = false){
        val intent = Intent(context, activityClass)
        if (null != bundle) {
            intent.putExtras(bundle)
        }
        context.startActivity(intent)
        if(context is Activity){
            if (finish) context.finish()
        }
    }
}