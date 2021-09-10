package com.roll.codemao.utils.dialog;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;


/**
 * Author by cretin, Email mxnzp_life@163.com, Date on 2018/4/19.
 * PS: Not easy to write code, please care for the programmer.
 * Description:通用opupWindow
 */
public class CommonPopupWindow extends PopupWindow {
    //主布局
    private View mMenuView;
    //动画
    private int animationStyle;
    //背景颜色
    private int backgroundColor;
    //View加载的事件监听
    private ViewLoadListener loadListener;

    @Override
    public int getAnimationStyle() {
        return animationStyle;
    }

    @Override
    public void setAnimationStyle(int animationStyle) {
        this.animationStyle = animationStyle;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public ViewLoadListener getLoadListener() {
        return loadListener;
    }

    public void setLoadListener(ViewLoadListener loadListener) {
        this.loadListener = loadListener;
    }

    public CommonPopupWindow(Activity context, int layoutId, ViewLoadListener loadListener) {
        mMenuView = View.inflate(context, layoutId, null);
        this.loadListener = loadListener;
        initView();
    }

    public CommonPopupWindow(Activity context, int layoutId) {
        mMenuView = View.inflate(context, layoutId, null);
        initView();
    }

    public CommonPopupWindow(Activity context, int layoutId, int animationStyle) {
        mMenuView = View.inflate(context, layoutId, null);
        this.animationStyle = animationStyle;
        initView();
    }

    public CommonPopupWindow(Activity context, int layoutId, int animationStyle, int backgroundColor) {
        mMenuView = View.inflate(context, layoutId, null);
        this.animationStyle = animationStyle;
        this.backgroundColor = backgroundColor;
        initView();
    }

    public CommonPopupWindow(Activity context, int layoutId, int animationStyle, int backgroundColor, ViewLoadListener loadListener) {
        mMenuView = View.inflate(context, layoutId, null);
        this.animationStyle = animationStyle;
        this.backgroundColor = backgroundColor;
        this.loadListener = loadListener;
        initView();
    }

    public CommonPopupWindow(View contentView, ViewLoadListener loadListener) {
        mMenuView = contentView;
        this.loadListener = loadListener;
        initView();
    }

    public CommonPopupWindow(View contentView) {
        mMenuView = contentView;
        initView();
    }

    public CommonPopupWindow(View contentView, int animationStyle, ViewLoadListener loadListener) {
        mMenuView = contentView;
        this.animationStyle = animationStyle;
        this.loadListener = loadListener;
        initView();
    }

    public CommonPopupWindow(View contentView, int animationStyle, int backgroundColor, ViewLoadListener loadListener) {
        mMenuView = contentView;
        this.animationStyle = animationStyle;
        this.backgroundColor = backgroundColor;
        this.loadListener = loadListener;
        initView();
    }

    //初始化视图
    private void initView() {
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        if (animationStyle != 0) {
            this.setAnimationStyle(animationStyle);
        } else {
//            this.setAnimationStyle(R.style.dialog_style);
        }
        if (backgroundColor != 0) {
            //实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(backgroundColor);
            //设置SelectPicPopupWindow弹出窗体的背景
            this.setBackgroundDrawable(dw);
        } else {
            //实例化一个ColorDrawable颜色为半透明
            ColorDrawable dw = new ColorDrawable(0xb0000000);
            //设置SelectPicPopupWindow弹出窗体的背景
            this.setBackgroundDrawable(dw);
        }

        if (loadListener != null)
            loadListener.onViewLoad(mMenuView, this);
    }

    public interface ViewLoadListener {
        void onViewLoad(View view, PopupWindow popupWindow);
    }
}
