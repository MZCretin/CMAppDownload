package com.roll.codemao.base.ui.helper

import android.app.Activity
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import android.widget.ImageView
import java.io.File

interface ImageEngineCallback {
    enum class CornerType {
        ALL, TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, TOP, BOTTOM, LEFT, RIGHT, OTHER_TOP_LEFT, OTHER_TOP_RIGHT, OTHER_BOTTOM_LEFT, OTHER_BOTTOM_RIGHT, DIAGONAL_FROM_TOP_LEFT, DIAGONAL_FROM_TOP_RIGHT
    }

    /**
     * 加载图片
     */
    fun loadImage(resType: Int = 0,
                  imageView: ImageView,
                  urlPath: String?,
                  localFile: File?,
                  @DrawableRes resId: Int,
                  drawable: Drawable?,
                  activity: Activity? = null,
                  fragment: Fragment? = null,
                  roundDp: Float = ImageHelper.DEFAULT_ROUND_SIZE,
                  cornerType: CornerType? = null,
                  @DrawableRes placeHolderRes: Int,
                  @DrawableRes errorHolderRes: Int,
                  placeHolder: Drawable? = null,
                  errorHolder: Drawable? = null,
                  skip: Boolean)
}