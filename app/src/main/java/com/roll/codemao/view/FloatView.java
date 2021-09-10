package com.roll.codemao.view;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.roll.codemao.R;

/**
 * @date: on 2019-11-28
 * @author: a112233
 * @email: mxnzp_life@163.com
 * @desc: 添加描述
 */
public class FloatView extends LinearLayout {
    private LinearLayout llRoot;
    private Context context;
    private View contentView;
    private OnDismissListener dismissListener;

    public void setDismissListener(OnDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    public FloatView(Context context) {
        this(context, null, 0);
    }

    public FloatView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(final Context context) {
        this.context = context;
        View.inflate(context, R.layout.layout_float_view_container, this);
        llRoot = findViewById(R.id.ll_root);
        llRoot.setBackgroundColor(Color.parseColor("#88000000"));
        setBackgroundColor(Color.TRANSPARENT);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     * 添加View到内容区
     *
     * @param contentView
     */
    public void addContentView(final View contentView, int height) {
        this.contentView = contentView;
        if (context instanceof Activity) {
            llRoot.removeAllViews();
            llRoot.addView(contentView, new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
        }
        this.contentView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * 展示在目标View之下
     *
     * @param aimView
     */
    public void showBelow(final View aimView) {
        final int[] position = new int[2];  //保存当前坐标的数组
        aimView.getLocationOnScreen(position);

        Point outSize = new Point();
        ((Activity) context).getWindowManager().getDefaultDisplay().getRealSize(outSize);
        int y = outSize.y;

        int height = aimView.getHeight();

        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        int dimensionPixelSize = getResources().getDimensionPixelSize(resourceId);

        int topHeight = height + position[1];

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if ((WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS & ((Activity) context).getWindow().getAttributes().flags)
                    == WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) {

            } else {
                topHeight = topHeight - dimensionPixelSize;
            }
        } else {
            topHeight = topHeight - dimensionPixelSize;
        }

        if (context instanceof AppCompatActivity) {
            ActionBar supportActionBar = ((AppCompatActivity) context).getSupportActionBar();
            if (supportActionBar != null && supportActionBar.isShowing()) {
                topHeight = topHeight - supportActionBar.getHeight();
            }
        }

        int realHeight = y;

        LayoutParams layoutParams = (LayoutParams) llRoot.getLayoutParams();
        layoutParams.setMargins(0, topHeight, 0, 0);
        llRoot.setLayoutParams(layoutParams);

        ((FrameLayout) ((Activity) context).findViewById(android.R.id.content)).removeView(this);
        ((FrameLayout) ((Activity) context).findViewById(android.R.id.content)).addView(this, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, realHeight));

        //透明度动画
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "alpha", 0, 1);
        animator.setDuration(300);
        animator.start();
    }

    public interface OnDismissListener {
        void onDismiss(boolean isDismiss);
    }

    public void dismiss() {
        ((FrameLayout) ((Activity) context).findViewById(android.R.id.content)).removeView(this);
        if (dismissListener != null) {
            dismissListener.onDismiss(true);
        }
    }
}
