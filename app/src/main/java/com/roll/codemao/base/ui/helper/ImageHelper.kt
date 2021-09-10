package com.roll.codemao.base.ui.helper

import android.app.Activity
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import android.widget.ImageView
import com.roll.codemao.R
import java.io.File


/**
 * 图片加载器
 */
object ImageHelper {
    val DEFAULT_ROUND_SIZE = 5f

    var imageEngine: ImageEngineCallback? = null

    //请先设置
    fun bindImageEngine(engine: ImageEngineCallback) {
        this.imageEngine = engine
    }
}

data class ImageConfig(var imageView: ImageView) {
    internal var activity: Activity? = null
    internal var fragment: Fragment? = null
    internal var placeDrawableRes: Int = R.mipmap.place_holder
    internal var placeDrawable: Drawable? = null
    internal var errorDrawableRes: Int = R.mipmap.place_holder
    internal var errorDrawable: Drawable? = null
    internal var skipCache: Boolean = false
    internal var roundDp: Float = 0f
    internal var cornerType: ImageEngineCallback.CornerType? = null

    fun bindActivity(activity: Activity): ImageConfig {
        this.activity = activity
        return this
    }

    fun bindFragment(fragment: Fragment?): ImageConfig {
        this.fragment = fragment
        return this
    }

    fun bindPlaceHolder(@DrawableRes placeHolder: Int = R.mipmap.place_holder): ImageConfig {
        this.placeDrawableRes = placeHolder
        return this
    }

    fun bindPlaceHolder(placeHolder: Drawable?): ImageConfig {
        this.placeDrawable = placeHolder
        return this
    }

    fun bindErrorHolder(@DrawableRes errorHolder: Int = R.mipmap.place_holder): ImageConfig {
        this.errorDrawableRes = errorDrawableRes
        return this
    }

    fun bindErrorHolder(errorHolder: Drawable?): ImageConfig {
        this.errorDrawable = errorHolder
        return this
    }

    fun bindCornerType(roundDp: Float, cornerType: ImageEngineCallback.CornerType): ImageConfig {
        this.roundDp = roundDp
        this.cornerType = cornerType
        return this
    }

    fun skipCache(skip: Boolean): ImageConfig {
        this.skipCache = skip
        return this
    }
}

fun ImageView.createConfig(): ImageConfig {
    return ImageConfig(this)
}

/**
 * 显示网络图片
 */
fun ImageView.loadNetImage(urlPath: String) {
    loadImage(0, this, urlPath, null, 0, null,
            null, null, 0f, null, 0,
            0, null, null, false)
}

/**
 * 显示本地图片
 */
fun ImageView.loadLocalImage(@DrawableRes imageRes: Int) {
    loadImage(2, this, "", null, imageRes, null,
            null, null, 0f, null, 0,
            0, null, null, false)
}

/**
 * 显示本地图片
 */
fun ImageView.loadLocalImage(localPath: String) {
    loadImage(1, this, "", File(localPath), 0, null,
            null, null, 0f, null, 0,
            0, null, null, false)
}

/**
 * 显示本地图片
 */
fun ImageView.loadLocalImage(imageDrawable: Drawable) {
    loadImage(3, this, "", null, 0, imageDrawable,
            null, null, 0f, null, 0,
            0, null, null, false)
}

/**
 * 显示网络图片
 */
fun ImageConfig.loadNetImage(urlPath: String) {
    loadImage(0, this.imageView, urlPath, null, 0, null,
            this.activity, this.fragment, this.roundDp, this.cornerType, this.placeDrawableRes,
            this.errorDrawableRes, this.placeDrawable, this.errorDrawable, skipCache)
}

/**
 * 显示本地图片
 */
fun ImageConfig.loadLocalImage(@DrawableRes imageRes: Int) {
    loadImage(2, this.imageView, "", null, imageRes, null,
            this.activity, this.fragment, this.roundDp, this.cornerType, this.placeDrawableRes,
            this.errorDrawableRes, this.placeDrawable, this.errorDrawable, skipCache)
}

/**
 * 显示本地图片
 */
fun ImageConfig.loadLocalImage(localPath: String) {
    loadImage(1, this.imageView, "", File(localPath), 0, null,
            this.activity, this.fragment, this.roundDp, this.cornerType, this.placeDrawableRes,
            this.errorDrawableRes, this.placeDrawable, this.errorDrawable, skipCache)
}

/**
 * 显示本地图片
 */
fun ImageConfig.loadLocalImage(imageDrawable: Drawable) {
    loadImage(3, this.imageView, "", null, 0, imageDrawable,
            this.activity, this.fragment, this.roundDp, this.cornerType, this.placeDrawableRes,
            this.errorDrawableRes, this.placeDrawable, this.errorDrawable, skipCache)
}

/**
 * resType：0 远程图片 1 本地图片File 2 本地资源id 3 本地drawable
 */
private fun loadImage(resType: Int = 0,
                      imageView: ImageView,
                      urlPath: String?,
                      localFile: File?,
                      @DrawableRes resId: Int,
                      drawable: Drawable?,
                      activity: Activity? = null,
                      fragment: Fragment? = null,
                      roundDp: Float = 0f,
                      cornerType: ImageEngineCallback.CornerType? = null,
                      @DrawableRes placeHolderRes: Int,
                      @DrawableRes errorHolderRes: Int,
                      placeHolder: Drawable? = null,
                      errorHolder: Drawable? = null,
                      skip: Boolean) {
    val obtainCurrentImageEngine = obtainCurrentImageEngine()
    obtainCurrentImageEngine?.loadImage(resType, imageView, urlPath, localFile, resId, drawable, activity, fragment, roundDp, cornerType, placeHolderRes, errorHolderRes, placeHolder, errorHolder, skip)
}

/**
 * 获取图片加载引擎
 */
fun obtainCurrentImageEngine(): ImageEngineCallback? {
    return ImageHelper.imageEngine
}


