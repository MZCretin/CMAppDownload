package com.roll.codemao.utils.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.roll.codemao.R;
import com.roll.codemao.utils.ViewUtils;


/**
 * Created by cretin on 2018/4/4.
 * 项目中用到的所有的对话框都用这个生成
 * <p>
 * 这里对大部分情况做了封装 没有最完美的封装  遇到一些其他的情况的时候 根据使用的频率再考虑是否要添加功能
 */

public class CommDialogUtils {
    //-------------------------CommDialog start--------------------------

    /**
     * 展示默认的CommDialog
     *
     * @param context
     * @return
     */
    public static CommDialog showCommDialog(Context context) {
        return new CommDialog(context);
    }

    /**
     * 展示layoutId的CommDialog
     *
     * @param context
     * @param layoutId
     * @param type
     * @return
     */
    public static CommDialog showCommDialog(Context context, int layoutId, int type) {
        return new CommDialog(context, layoutId, type);
    }

    /**
     * 展示layoutId的CommDialog 带事件监听
     *
     * @param context
     * @param layoutId
     * @param loadListener
     * @return
     */
    public static CommDialog showCommDialog(Context context, int layoutId, ViewLoadListener loadListener) {
        return new CommDialog(context, layoutId, loadListener);
    }

    /**
     * 展示layoutId的CommDialog 带事件监听
     *
     * @param context
     * @param layoutId
     * @param loadSurfListener
     * @return
     */
    public static CommDialog showCommDialog(Context context, int layoutId, ViewLoadSurfListener loadSurfListener) {
        return new CommDialog(context, layoutId, loadSurfListener);
    }

    /**
     * 展示contentView的CommDialog
     *
     * @param context
     * @param contentView
     * @return
     */
    public static CommDialog showCommDialog(Context context, View contentView) {
        return new CommDialog(context, contentView);
    }

    /**
     * 展示默认的CommDialog 自定义标题和内容
     *
     * @param title
     * @param message
     * @return
     */
    public static CommDialog showCommDialog(Context context, String title, String message) {
        return new CommDialog(context, title, message);
    }

    public static CommDialog showCommDialog(Context context, String title, String message, ViewLoadSurfListener listener) {
        return new CommDialog(context, title, message, listener);
    }

    /**
     * 展示默认的CommDialog 自定义标题和内容 以及类型
     *
     * @param context
     * @param title
     * @param message
     * @param type
     * @return
     */
    public static CommDialog showCommDialog(Context context, String title, String message, int type) {
        return new CommDialog(context, title, message, type);
    }

    /**
     * 展示默认的CommDialog 自定义标题和内容和右边按钮的文字
     *
     * @param context
     * @param title
     * @param message
     * @param rightBtnMsg
     * @return
     */
    public static CommDialog showCommDialog(Context context, String title, String message, String rightBtnMsg) {
        return new CommDialog(context, title, message, rightBtnMsg);
    }

    /**
     * 展示默认的CommDialog 自定义标题和内容和右边按钮的文字 并指定样式
     *
     * @param context
     * @param title
     * @param message
     * @param rightBtnMsg
     * @param type
     * @return
     */
    public static CommDialog showCommDialog(Context context, String title, String message, String rightBtnMsg, int type) {
        return new CommDialog(context, title, message, rightBtnMsg, type);
    }

    /**
     * 展示默认的CommDialog 自定义标题和内容和左边和右边按钮的文字 并指定样式
     *
     * @param context
     * @param title
     * @param message
     * @param leftBtnMsg
     * @param rightBtnMsg
     * @return
     */
    public static CommDialog showCommDialog(Context context, String title, String message, String leftBtnMsg, String rightBtnMsg) {
        return new CommDialog(context, title, message, leftBtnMsg, rightBtnMsg);
    }
    //-------------------------CommDialog end--------------------------

    //-------------------------CommDialogFragment start--------------------------

    /**
     * 用自定义的View来显示CommDialogFragment
     *
     * @param resId
     * @return
     */
    public static CommDialogFragment showCustomDialogFragment(int resId) {
        return new CommDialogFragment(resId);
    }

    /**
     * 用自定义的View来显示CommDialogFragment
     *
     * @param windowAnimations
     * @param resId
     * @return
     */
    public static CommDialogFragment showCustomDialogFragment(int windowAnimations, int resId) {
        return new CommDialogFragment(windowAnimations, resId);
    }

    /**
     * 用自定义的View来显示CommDialogFragment
     *
     * @param windowAnimations
     * @param resId
     * @param loadSurfListener
     * @return
     */
    public static CommDialogFragment showCustomDialogFragment(int windowAnimations, int resId, ViewLoadSurfListener loadSurfListener) {
        return new CommDialogFragment(windowAnimations, resId, loadSurfListener);
    }

    /**
     * 用自定义的View来显示CommDialogFragment
     *
     * @param resId
     * @param background
     * @return
     */
    public static CommDialogFragment showCustomDialogFragment(int resId, Drawable background) {
        return new CommDialogFragment(resId, background);
    }

    /**
     * 用自定义的View来显示CommDialogFragment
     *
     * @param windowAnimations
     * @param resId
     * @param background
     * @return
     */
    public static CommDialogFragment showCustomDialogFragment(int windowAnimations, int resId, Drawable background) {
        return new CommDialogFragment(windowAnimations, resId, background);
    }
    //-------------------------CommDialogFragment end--------------------------


    //-------------------------CsutomToast start--------------------------

    /**
     * 用自定义的View来显示Toast
     *
     * @param context
     * @param resId
     */
    public static void showCustomToast(final Context context, int resId) {
        showCustomToast(context, resId, null);
    }

    /**
     * 用自定义的View来显示Toast
     *
     * @param context
     * @param resId
     * @param listener 返回视图给用户操作
     */
    public static void showCustomToast(final Context context, int resId, ViewLoadListener listener) {
        Toast toast = new Toast(context);
        View view = LayoutInflater.from(context).inflate(resId, null);
        if ( listener != null )
            listener.onViewLoad(view);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    //-------------------------CsutomToast end--------------------------


    public interface ViewLoadListener {
        void onViewLoad(View view);
    }

    public interface ViewLoadSurfListener<T> {
        void onViewLoad(View view, T t);
    }


    @SuppressLint( "ValidFragment" )
    public static class CommDialogFragment extends DialogFragment {
        //动画
        private int windowAnimations;
        //视图文件
        private int resId;
        //背景
        private Drawable background;
        //视图获取事件监听
        private ViewLoadListener loadListener;
        private ViewLoadSurfListener loadSurfListener;

        private View contentView;

        public View getContentView() {
            return contentView;
        }

        public ViewLoadSurfListener getLoadSurfListener() {
            return loadSurfListener;
        }

        public void setLoadSurfListener(ViewLoadSurfListener loadSurfListener) {
            this.loadSurfListener = loadSurfListener;
        }

        public ViewLoadListener getLoadListener() {
            return loadListener;
        }

        public void setLoadListener(ViewLoadListener loadListener) {
            this.loadListener = loadListener;
        }

        public CommDialogFragment(int resId) {
            this.resId = resId;
        }

        public CommDialogFragment(int windowAnimations, int resId) {
            this.windowAnimations = windowAnimations;
            this.resId = resId;
        }

        public CommDialogFragment(int windowAnimations, int resId, ViewLoadSurfListener loadSurfListener) {
            this.windowAnimations = windowAnimations;
            this.resId = resId;
            this.loadSurfListener = loadSurfListener;
        }

        public CommDialogFragment(int resId, Drawable background) {
            this.resId = resId;
            this.background = background;
        }

        public CommDialogFragment(int windowAnimations, int resId, Drawable background) {
            this.windowAnimations = windowAnimations;
            this.resId = resId;
            this.background = background;
        }


        @Override
        public void onStart() {
            super.onStart();
            Window window = getDialog().getWindow();
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            if ( windowAnimations != 0 )
                layoutParams.windowAnimations = windowAnimations;
            window.setAttributes(layoutParams);
        }

        @Override
        public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if ( background != null ) {
                getDialog().getWindow().setBackgroundDrawable(background);
            } else {
                getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            //设置没有title
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            //获取布局文件并转化成view
            contentView = inflater.inflate(resId, container, false);
            //将视图回调给用户自己处理
            if ( loadListener != null )
                loadListener.onViewLoad(contentView);
            if ( loadSurfListener != null )
                loadSurfListener.onViewLoad(contentView, this);
            return contentView;
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setStyle(STYLE_NO_FRAME, R.style.Theme_AppCompat_Light_Dialog);
        }
    }

    /**
     * 主要是确认对话框 默认有布局 只需要设置文字和事件监听
     */
    public static class CommDialog extends Dialog implements
            View.OnClickListener {
        public static final int TYPE_INVITE_AWARD_DETAILS = 1;
        public static final int TYPE_SINGLE = 2;
        private int layoutId;
        private View contentView;
        private Context mContext;
        private String title;
        private String message;
        private String rightMsg;
        private String leftBtnMsg;
        private TextView mTitle;
        public TextView mContent;
        public TextView mBtnCancel;
        private TextView mBtnConfirm;
        private boolean positiveDismiss = true;
        private boolean negativeDismiss = true;
        private int type;
        //视图获取事件监听
        private ViewLoadListener loadListener;
        private ViewLoadSurfListener loadSurfListener;

        public ViewLoadSurfListener getLoadSurfListener() {
            return loadSurfListener;
        }

        public void setLoadSurfListener(ViewLoadSurfListener loadSurfListener) {
            this.loadSurfListener = loadSurfListener;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
            if ( mContent != null )
                mContent.setText(message);
        }

        public ViewLoadListener getLoadListener() {
            return loadListener;
        }

        public void setLoadListener(ViewLoadListener loadListener) {
            this.loadListener = loadListener;
        }

        public CommDialog(Context context) {
            super(context, R.style.Theme_AppCompat_Light_Dialog);
            this.mContext = context;
        }

        public CommDialog(Context context, int layoutId) {
            super(context, R.style.Theme_AppCompat_Light_Dialog);
            this.layoutId = layoutId;
            this.mContext = context;
        }

        public CommDialog(Context context, int layoutId, ViewLoadSurfListener loadSurfListener) {
            super(context, R.style.Theme_AppCompat_Light_Dialog);
            this.layoutId = layoutId;
            this.mContext = context;
            this.loadSurfListener = loadSurfListener;
        }

        public CommDialog(Context context, int layoutId, ViewLoadListener loadListener) {
            super(context, R.style.Theme_AppCompat_Light_Dialog);
            this.layoutId = layoutId;
            this.mContext = context;
            this.loadListener = loadListener;
        }

        public CommDialog(Context context, int layoutId, int type) {
            super(context, R.style.Theme_AppCompat_Light_Dialog);
            this.layoutId = layoutId;
            this.mContext = context;
            this.type = type;
        }

        public CommDialog(Context context, View contentView) {
            super(context, R.style.Theme_AppCompat_Light_Dialog);
            this.contentView = contentView;
            this.mContext = context;
        }

        public CommDialog(Context context, String title, String message) {
            super(context, R.style.Theme_AppCompat_Light_Dialog);
            this.title = title;
            this.message = message;
            this.mContext = context;
        }

        public CommDialog(Context context, String title, String message, ViewLoadSurfListener listener) {
            super(context, R.style.Theme_AppCompat_Light_Dialog);
            this.title = title;
            this.message = message;
            this.mContext = context;
            this.loadSurfListener = listener;
        }

        public CommDialog(Context context, String title, String message, int type) {
            super(context, R.style.Theme_AppCompat_Light_Dialog);
            this.title = title;
            this.message = message;
            this.mContext = context;
            this.type = type;
        }

        public CommDialog(Context context, String title, String message, String rightBtnMsg) {
            super(context, R.style.Theme_AppCompat_Light_Dialog);
            this.title = title;
            this.message = message;
            this.mContext = context;
            this.rightMsg = rightBtnMsg;
        }

        public CommDialog(Context context, String title, String message, String rightBtnMsg, int type) {
            super(context, R.style.Theme_AppCompat_Light_Dialog);
            this.title = title;
            this.message = message;
            this.mContext = context;
            this.rightMsg = rightBtnMsg;
            this.type = type;
        }

        public CommDialog(Context context, String title, String message, String leftBtnMsg, String rightBtnMsg) {
            super(context, R.style.Theme_AppCompat_Light_Dialog);
            this.title = title;
            this.message = message;
            this.mContext = context;
            this.rightMsg = rightBtnMsg;
            this.leftBtnMsg = leftBtnMsg;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            super.onCreate(savedInstanceState);
            getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            if ( type == TYPE_SINGLE ) {
                contentView = View.inflate(mContext, R.layout.common_dialog_single, null);
                if ( loadListener != null )
                    loadListener.onViewLoad(contentView);
                if ( loadSurfListener != null )
                    loadSurfListener.onViewLoad(contentView, this);
                setContentView(contentView);
                mTitle = (TextView) contentView.findViewById(R.id.dialog_title);
                mContent = (TextView) contentView.findViewById(R.id.dialog_content);
                mBtnConfirm = (TextView) contentView.findViewById(R.id.button_confirm);
                mTitle.setText(title);
                mContent.setText(message);
                if ( !TextUtils.isEmpty(rightMsg) ) {
                    mBtnConfirm.setText(rightMsg);
                }
                if ( !TextUtils.isEmpty(leftBtnMsg) ) {
                    mBtnCancel.setText(leftBtnMsg);
                }
                mBtnConfirm.setOnClickListener(this);
            } else {
                if ( layoutId != 0 ) {
                    contentView = View.inflate(mContext, layoutId, null);
                    if ( loadListener != null )
                        loadListener.onViewLoad(contentView);
                    if ( loadSurfListener != null )
                        loadSurfListener.onViewLoad(contentView, this);
                    setContentView(contentView);
                } else if ( contentView != null ) {
                    if ( loadListener != null )
                        loadListener.onViewLoad(contentView);
                    if ( loadSurfListener != null )
                        loadSurfListener.onViewLoad(contentView, this);
                    setContentView(contentView);
                } else {
                    contentView = View.inflate(mContext, R.layout.common_dialog, null);
                    setContentView(R.layout.common_dialog);
                    if ( loadListener != null )
                        loadListener.onViewLoad(contentView);
                    if ( loadSurfListener != null )
                        loadSurfListener.onViewLoad(contentView, this);
                    mTitle = (TextView) findViewById(R.id.dialog_title);
                    mContent = (TextView) findViewById(R.id.dialog_content);
                    mBtnConfirm = (TextView) findViewById(R.id.button_confirm);
                    mBtnCancel = (TextView) findViewById(R.id.button_cancel);
                    mTitle.setText(title);
                    mContent.setText(message);
                    if ( !TextUtils.isEmpty(rightMsg) ) {
                        mBtnConfirm.setText(rightMsg);
                    }
                    if ( !TextUtils.isEmpty(leftBtnMsg) ) {
                        mBtnCancel.setText(leftBtnMsg);
                    }

                    if ( loadSurfListener == null && loadListener == null ) {
                        mBtnConfirm.setOnClickListener(this);
                        mBtnCancel.setOnClickListener(this);
                    }
                }
            }
        }

        public void hideCancel() {
            if ( mBtnCancel != null ) {
                mBtnCancel.setVisibility(View.GONE);
            }
        }

        public void setContentViewHeight(int height) {
            if ( mContent != null ) {
                ViewGroup.LayoutParams layoutParams = mContent.getLayoutParams();
                layoutParams.height = ( int ) (height * ViewUtils.getScreenScale(mContext));
                mContent.setLayoutParams(layoutParams);
            }
        }

        OnPositiveClickListener mClickListener;

        public interface OnPositiveClickListener {
            void onPositiveClickListener(View v);

        }

        public interface OnNegativeClickListener {
            void onNegativeClickListener(View v);
        }

        private OnNegativeClickListener mNegativeListener;

        public CommDialog setOnNegativeListener(
                OnNegativeClickListener mNegativeListener) {
            this.mNegativeListener = mNegativeListener;
            return this;
        }

        public CommDialog setOnClickListener(OnPositiveClickListener mClickListener) {
            this.mClickListener = mClickListener;
            return this;
        }

        public void setPositiveDismiss(boolean flag) {
            positiveDismiss = flag;
        }

        public void setNegativeDismiss(boolean flag) {
            negativeDismiss = flag;
        }

        @Override
        public void onClick(View v) {
            switch ( v.getId() ) {
                case R.id.button_cancel:
                    if ( mNegativeListener != null ) {
                        mNegativeListener.onNegativeClickListener(v);
                    }
                    if ( negativeDismiss ) {
                        this.dismiss();
                    }
                    break;
                case R.id.button_confirm:
                    if ( mClickListener != null ) {
                        if ( positiveDismiss ) {
                            this.dismiss();
                        }
                        mClickListener.onPositiveClickListener(v);
                    }
                    break;
                default:
                    break;
            }
        }
    }

}
