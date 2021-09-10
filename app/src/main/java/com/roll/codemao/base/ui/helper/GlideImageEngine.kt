package com.roll.codemao.base.ui.helper

import android.app.Activity
import android.graphics.drawable.Drawable
import androidx.fragment.app.Fragment
import android.widget.ImageView
import com.blankj.utilcode.util.ConvertUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import java.io.File

class GlideImageEngine : ImageEngineCallback {

    /**
     * 实现加载图片
     * resType：0 远程图片 1 本地图片File 2 本地资源id 3 本地drawable
     */
    override fun loadImage(resType: Int,
                           imageView: ImageView,
                           urlPath: String?,
                           localFile: File?,
                           resId: Int,
                           drawable: Drawable?,
                           activity: Activity?,
                           fragment: Fragment?,
                           roundDp: Float,
                           cornerType: ImageEngineCallback.CornerType?,
                           placeHolderRes: Int,
                           errorHolderRes: Int,
                           placeHolder: Drawable?,
                           errorHolder: Drawable?,
                           skip: Boolean) {
        var requestManager = if (fragment != null) {
            Glide.with(fragment)
        } else if (activity != null) {
            Glide.with(activity)
        } else {
            Glide.with(imageView.context)
        }

        var requestOptions: RequestOptions? = null
        //配置placeHolder数据
        if (placeHolder != null) {
            if (requestOptions == null) {
                requestOptions = RequestOptions()
            }
            requestOptions.placeholder(placeHolder)
        } else if (placeHolderRes != 0) {
            if (requestOptions == null) {
                requestOptions = RequestOptions()
            }
            requestOptions.placeholder(placeHolderRes)
        }
        //配置err
        if (errorHolder != null) {
            if (requestOptions == null) {
                requestOptions = RequestOptions()
            }
            requestOptions.error(errorHolder)
        } else if (errorHolderRes != 0) {
            if (requestOptions == null) {
                requestOptions = RequestOptions()
            }
            requestOptions.error(errorHolderRes)
        }

        if (skip == true) {
            if (requestOptions == null) {
                requestOptions = RequestOptions()
            }
            //设置是否跳过skip
            requestOptions?.skipMemoryCache(skip)
        }

        //设置圆角
        if (cornerType != null && roundDp != 0f) {
            if (requestOptions == null) {
                requestOptions = RequestOptions()
            }
            requestOptions.transform(RoundedCornersTransformation(ConvertUtils.dp2px(roundDp), 0, cornerType))
        }

        //真正的加载图片
        //resType：0 远程图片 1 本地图片File 2 本地资源id 3 本地drawable
        var requestBuilder =
                if (resType == 3) {
                    requestManager?.load(drawable)
                } else if (resType == 1) {
                    requestManager?.load(localFile)
                } else if (resType == 2) {
                    requestManager?.load(resId)
                } else {
                    requestManager?.load(urlPath)
                }

        if (requestOptions != null) {
            requestBuilder = requestBuilder.apply(requestOptions)
        }

        requestBuilder.into(imageView)
    }
}