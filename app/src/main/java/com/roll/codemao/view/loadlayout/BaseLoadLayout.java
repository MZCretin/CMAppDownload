package com.roll.codemao.view.loadlayout;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.roll.codemao.R;


/**
 * author：    zp
 * date：      2015/10/6 & 10:51
 * version     1.0
 * description:
 * modify by   ljy
 */
public abstract class BaseLoadLayout extends FrameLayout implements State {
    /**
     * 加载动画类
     */
    private AnimationDrawable mAnimationDrawable;
    private View mSuccessView;
    protected View mLoadingView;
    protected View mFailedView;
    //    protected View mNoDataView;
    private int mState = State.SUCCESS;
    private Context context;

    protected abstract View createLoadingView();

    protected abstract View createLoadFailedView();

    protected abstract View createNoDataView();

    public BaseLoadLayout(Context context) {
        this(context, null, 0);
    }

    public int getState() {
        return mState;
    }

    public BaseLoadLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseLoadLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public void setContentView(View contentView) {
        this.mSuccessView = contentView;
        if (mSuccessView != null) {
            addView(mSuccessView,0, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            setLayoutState(State.LOADING);
        }
    }

    public void setContentView(int contentView) {
        setContentView(LayoutInflater.from(context).inflate(contentView, this));
    }

    /**
     * 加载中
     */
    private void onLoading() {
        if (mFailedView != null) {
            removeView(mFailedView);
        }
//        if (mNoDataView != null) {
//            removeView(mNoDataView);
//        }
        if (mLoadingView != null) {
            removeView(mLoadingView);
        } else {
            mLoadingView = createLoadingView();
        }
        addView(mLoadingView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        initAnim(mLoadingView);
        startAnim();
    }

    public void retry() {
        if (loadFailedRetryListener != null)
            loadFailedRetryListener.onLoadFailed();
    }

    /**
     * 加载失败
     */
    private void onLoadFailed() {
//        if (mNoDataView != null) {
//            removeView(mNoDataView);
//        }
        if (mLoadingView != null) {
            removeView(mLoadingView);
        }
        if (mFailedView != null) {
            removeView(mFailedView);
        } else {
            mFailedView = createLoadFailedView();
        }
        addView(mFailedView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        stopAnim();
    }


    private LoadFailedRetryListener loadFailedRetryListener;

    public void setLoadFailedRetryListener(LoadFailedRetryListener loadFailedRetryListener) {
        this.loadFailedRetryListener = loadFailedRetryListener;
    }

    /**
     * 无数据处理
     */
    private void onLoadNoData() {
//        if (mSuccessView != null) {
//            removeView(mSuccessView);
//        }
//        if (mLoadingView != null) {
//            removeView(mLoadingView);
//        }
//        if (mFailedView != null) {
//            removeView(mFailedView);
//        }
//        if (mNoDataView != null) {
//            removeView(mNoDataView);
//        } else {
//            mNoDataView = createNoDataView();
//        }
//        addView(mNoDataView);
//        stopAnim();
    }

    /**
     * 加载成功
     */
    private void onLoadSuccess() {
        if (mLoadingView != null) {
            removeView(mLoadingView);
        }
        if (mFailedView != null) {
            removeView(mFailedView);
        }
//        if (mNoDataView != null) {
//            removeView(mNoDataView);
//        }
        stopAnim();
    }

    public synchronized void setLayoutState(final int state) {
        this.mState = state;

        switch (this.mState) {
            case LOADING:
                onLoading();
                break;
            case FAILED:
                onLoadFailed();
                break;
            case SUCCESS:
                onLoadSuccess();
                break;
            case NO_DATA:
                onLoadNoData();
                break;
            default:
                break;
        }
    }

    public int getLayoutState() {
        return mState;
    }

    private void initAnim(View view) {
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_loading_kk);
        if (imageView.getBackground() != null) {
            mAnimationDrawable = (AnimationDrawable) imageView.getBackground();
        }
    }

    public void startAnim() {
        if (mAnimationDrawable != null) {
            mAnimationDrawable.start();
        }
    }

    public void stopAnim() {
        if (mAnimationDrawable != null) {
            mAnimationDrawable.stop();
        }
    }
}
