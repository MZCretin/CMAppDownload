package com.roll.codemao.base.ui.expand.glide

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import com.blankj.utilcode.util.ConvertUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.roll.codemao.base.ui.helper.ImageEngineCallback
import com.roll.codemao.base.ui.helper.RoundedCornersTransformation
import com.roll.codemao.ui.MainActivity

/**
 * @date: on 2019-11-21
 * @author: cretin
 * @email: mxnzp_life@163.com
 * @desc: 添加描述
 */

object GlideHelper {
    val TYPE_ALL = 0
    val TYPE_LEFT_TOP = 1
    val TYPE_RIGHT_TOP = 2
    val TYPE_LEFT_BOTTOM = 3
    val TYPE_RIGHT_BOTTOM = 4
    val TYPE_LEFT_RIGHT_TOP = 5
    val TYPE_LEFT_RIGHT_BOTTOM = 6
    val TYPE_LEFT_TOP_BOTTOM = 7
    val TYPE_RIGHT_TOP_BOTTOM = 8
    val TYPE_A_B_C = 9
    val TYPE_B_C_D = 10
    val TYPE_C_D_A = 11
    val TYPE_D_A_B = 12
    val TYPE_LEFT_TOP_TO_RIGHT_BOTTOM = 13
    val TYPE_LEFT_BOTTOM_TO_RIGHT_TOP = 14

    /**
     * 加载普通图片
     */
    fun loadNormalImage(context: Activity, image: ImageView, picUrl: String) {
        Glide.with(context).load(picUrl).into(image)
    }

    fun loadNormalImage(context: Fragment, image: ImageView, picUrl: String) {
        Glide.with(context).load(picUrl).into(image)
    }

    fun loadNormalImage(context: View, image: ImageView, picUrl: String) {
        Glide.with(context).load(picUrl).into(image)
    }

    fun loadNormalImage(context: Context, image: ImageView, picUrl: String) {
        Glide.with(context).load(picUrl).into(image)
    }

    /**
     * 默认10dp 四个角都是圆角
     */
    fun loadRoundImage(context: Activity, image: ImageView, picUrl: String) {
        loadRoundImage(context, image, picUrl, 10F, TYPE_ALL)
    }

    /**
     * 默认10dp 四个角都是圆角
     */
    fun loadRoundImage(context: Activity, image: ImageView, drawable: Drawable) {
        loadRoundImage(context, image, drawable, 10F, TYPE_ALL)
    }

    fun loadRoundImage(context: Fragment, image: ImageView, picUrl: String) {
        loadRoundImage(context, image, picUrl, 10F, TYPE_ALL)
    }

    fun loadRoundImage(context: View, image: ImageView, picUrl: String) {
        loadRoundImage(context, image, picUrl, 10F, TYPE_ALL)
    }

    fun loadRoundImage(context: Context, image: ImageView, picUrl: String) {
        loadRoundImage(context, image, picUrl, 10F, TYPE_ALL)
    }

    fun loadRoundImage(context: Activity, image: ImageView, picUrl: String, roundDp: Float = 10F, roundType: Int) {
        val requestOptions = RequestOptions()
        requestOptions.transform(getTrasnform(roundDp,roundType))
        Glide.with(context).load(picUrl).apply(requestOptions).into(image)
    }

    fun loadRoundImage(context: Activity, image: ImageView, drawable: Drawable, roundDp: Float = 10F, roundType: Int) {
        val requestOptions = RequestOptions()
        requestOptions.transform(getTrasnform(roundDp,roundType))
        Glide.with(context).load(drawable).apply(requestOptions).into(image)
    }

    fun loadRoundImage(context: Fragment, image: ImageView, picUrl: String, roundDp: Float = 10F, roundType: Int) {
        val requestOptions = RequestOptions()
        requestOptions.transform(getTrasnform(roundDp,roundType))
        Glide.with(context).load(picUrl).apply(requestOptions).into(image)
    }

    fun loadRoundImage(context: View, image: ImageView, picUrl: String, roundDp: Float = 10F, roundType: Int) {
        val requestOptions = RequestOptions()
        requestOptions.transform(getTrasnform(roundDp,roundType))
        Glide.with(context).load(picUrl).apply(requestOptions).into(image)
    }

    fun loadRoundImage(context: Context, image: ImageView, picUrl: String, roundDp: Float = 10F, roundType: Int) {
        val requestOptions = RequestOptions()
        requestOptions.transform(getTrasnform(roundDp,roundType))
        Glide.with(context).load(picUrl).apply(requestOptions).into(image)
    }

    private fun getTrasnform(roundDp:Float,roundType: Int): RoundedCornersTransformation {
        when (roundType) {
            TYPE_ALL -> {
                return RoundedCornersTransformation(ConvertUtils.dp2px(roundDp), 0, ImageEngineCallback.CornerType.ALL)
            }
            TYPE_LEFT_TOP -> {
                return RoundedCornersTransformation(ConvertUtils.dp2px(roundDp), 0, ImageEngineCallback.CornerType.TOP_LEFT)
            }
            TYPE_RIGHT_TOP -> {
                return RoundedCornersTransformation(ConvertUtils.dp2px(roundDp), 0, ImageEngineCallback.CornerType.TOP_RIGHT)
            }
            TYPE_LEFT_BOTTOM -> {
                return RoundedCornersTransformation(ConvertUtils.dp2px(roundDp), 0, ImageEngineCallback.CornerType.BOTTOM_LEFT)
            }
            TYPE_RIGHT_BOTTOM -> {
                return RoundedCornersTransformation(ConvertUtils.dp2px(roundDp), 0, ImageEngineCallback.CornerType.BOTTOM_RIGHT)
            }
            TYPE_LEFT_RIGHT_TOP -> {
                return RoundedCornersTransformation(ConvertUtils.dp2px(roundDp), 0, ImageEngineCallback.CornerType.TOP)
            }
            TYPE_LEFT_RIGHT_BOTTOM -> {
                return RoundedCornersTransformation(ConvertUtils.dp2px(roundDp), 0, ImageEngineCallback.CornerType.BOTTOM)
            }
            TYPE_LEFT_TOP_BOTTOM -> {
                return RoundedCornersTransformation(ConvertUtils.dp2px(roundDp), 0, ImageEngineCallback.CornerType.LEFT)
            }
            TYPE_RIGHT_TOP_BOTTOM -> {
                return RoundedCornersTransformation(ConvertUtils.dp2px(roundDp), 0, ImageEngineCallback.CornerType.RIGHT)
            }
            TYPE_A_B_C -> {
                return RoundedCornersTransformation(ConvertUtils.dp2px(roundDp), 0, ImageEngineCallback.CornerType.OTHER_BOTTOM_LEFT)
            }
            TYPE_B_C_D -> {
                return RoundedCornersTransformation(ConvertUtils.dp2px(roundDp), 0, ImageEngineCallback.CornerType.OTHER_TOP_LEFT)
            }
            TYPE_C_D_A -> {
                return RoundedCornersTransformation(ConvertUtils.dp2px(roundDp), 0, ImageEngineCallback.CornerType.OTHER_TOP_RIGHT)
            }
            TYPE_D_A_B -> {
                return RoundedCornersTransformation(ConvertUtils.dp2px(roundDp), 0, ImageEngineCallback.CornerType.OTHER_BOTTOM_RIGHT)
            }
            TYPE_LEFT_TOP_TO_RIGHT_BOTTOM -> {
                return RoundedCornersTransformation(ConvertUtils.dp2px(roundDp), 0, ImageEngineCallback.CornerType.DIAGONAL_FROM_TOP_LEFT)
            }
            TYPE_LEFT_BOTTOM_TO_RIGHT_TOP -> {
                return RoundedCornersTransformation(ConvertUtils.dp2px(roundDp), 0, ImageEngineCallback.CornerType.DIAGONAL_FROM_TOP_RIGHT)
            }
            else ->{
                return RoundedCornersTransformation(ConvertUtils.dp2px(roundDp), 0, ImageEngineCallback.CornerType.ALL)
            }
        }
    }


}

