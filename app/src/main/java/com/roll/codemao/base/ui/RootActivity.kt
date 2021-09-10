package com.roll.codemao.base.ui

import android.content.Context
import android.content.res.Resources
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.roll.codemao.R
import com.roll.codemao.app.data.api.model.event.NotifyNetStateChange
import com.roll.codemao.base.ui.expand.qmui.createTipDialog
import com.roll.codemao.base.ui.helper.PageJumpHelper
import com.roll.codemao.utils.ToastHelper
import com.roll.codemao.view.loadlayout.LoadLayout
import com.roll.codemao.view.loadlayout.State
import com.gyf.barlibrary.ImmersionBar
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import com.roll.codemao.base.ui.helper.HeaderViewHelper
import com.roll.codemao.view.HeaderViewContainer
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import me.jessyan.autosize.AutoSizeCompat
import me.jessyan.autosize.internal.CancelAdapt
import org.greenrobot.eventbus.EventBus

/**
 * Created by cretin.
 * 这个是Activity的根处理器 这里需要配置一些每个页面都需要的东西
 * 这个页面是不支持网络请求的
 * toastHelper toast 显示
 */
abstract class RootActivity<RB : ViewDataBinding?> : RxAppCompatActivity(), BaseView {
    //当前页面的对话框
    private var mDialog: QMUITipDialog? = null
    private var mBinding: RB? = null
    private var mLoadLayout: LoadLayout? = null
    var headerViewHelper: HeaderViewHelper? = null
    private var headerViewContainer: HeaderViewContainer? = null

    @JvmField
    var isKitkat = false
    var mActivity: RootActivity<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //隐藏ActionBar
        supportActionBar?.hide()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            isKitkat = true
        }

        //初始化状态栏
        if (useStatusBarInit()) {
            ImmersionBar.with(this)
                    .statusBarDarkFont(true, 0.2f)
                    .statusBarColor(R.color.transparent)
                    .fitsSystemWindows(false)
                    .keyboardEnable(true)
                    .init()
        }

        //处理视图
        handlerView(savedInstanceState)

        //如果需要eventbus 帮他注册
        if (useEventBus()) {
            EventBus.getDefault().register(this)
        }
    }

    private fun handlerView(savedInstanceState: Bundle?) {
        val view = layoutInflater.inflate(R.layout.layout_base, null)
        setContentView(view)
        initContentView(view, savedInstanceState)
        initListener()
    }

    private fun initContentView(view: View, savedInstanceState: Bundle?) {
        mLoadLayout = view.findViewById<View>(R.id.ll_base) as LoadLayout
        if (contentViewId != 0) {
            mBinding = createBinding()

            //在这里要处理下通用头部的逻辑
            if (useCommonHeader()) {
                headerViewHelper = HeaderViewHelper()
                headerViewContainer = HeaderViewContainer(this)
                headerViewContainer?.setContentView(mBinding!!.root)
                headerViewHelper?.bindHeaderView(headerViewContainer!!)

                if(isKitkat){
                    headerViewContainer?.setHeaderPaddingTop(BarUtils.getStatusBarHeight())
                }

                mLoadLayout?.setContentView(headerViewContainer)
                initView(mBinding?.root, savedInstanceState)
            } else {
                mLoadLayout?.setContentView(mBinding!!.root)
                initView(mBinding?.root, savedInstanceState)
            }
        } else {
            throw RuntimeException("请设置布局文件")
        }
        mLoadLayout!!.setLoadFailedRetryListener {
            showLoadingView()
            refreshPageData()
        }
    }

    /**
     * 展示主视图
     */
    fun showContentView() {
        //隐藏掉整个加载中和错误页面
        mLoadLayout?.layoutState = State.SUCCESS
    }

    /**
     * 显示正在加载视图
     */
    fun showLoadingView() {
        mLoadLayout?.layoutState = State.LOADING
    }

    private fun createBinding(): RB {
        return DataBindingUtil.inflate<RB>(LayoutInflater.from(this), contentViewId, null, false)
    }

    fun mBinding(): RB {
        if (mBinding == null) {
            mBinding = createBinding()
        }
        return mBinding!!
    }

    open fun notifyNetState(change: NotifyNetStateChange) {
        changeStateAndRefreshData(change)
    }

    internal fun changeStateAndRefreshData(change: NotifyNetStateChange) {
        if (change.isState) {
            //网络连接了
            if (mLoadLayout?.state == State.FAILED) {
                mLoadLayout?.layoutState = State.LOADING
                refreshPageData()
            }
        }
    }

    /**
     * 如果没有设置布局id 则不调用此方法
     *
     * @param view
     */
    protected abstract fun initView(view: View?, savedInstanceState: Bundle?)

    /**
     * 显示加载错误的视频
     */
    fun showErrorView() {
        mLoadLayout!!.layoutState = State.FAILED
    }

    abstract val contentViewId: Int

    /**
     * 将整个页面需要在刷新时加载的数据操作都放在这个里面 当出现无网络界面
     * 点击屏幕刷新数据的时候会调用里面的方法 在数据加载完请记得调用showContent方法
     */
    abstract fun refreshPageData()

    /**
     * 显示加载对话框
     *
     * @param msg
     */
    fun showDialog(msg: String? = "", cancelable: Boolean? = false) {
        mDialog = creataDialog(msg ?: "")
        mDialog?.setCancelable(cancelable == true)
        mDialog?.show()
    }

    internal fun creataDialog(msg: String?): QMUITipDialog? {
        mDialog = createTipDialog(msg ?: "", QMUITipDialog.Builder.ICON_TYPE_LOADING)
        return mDialog
    }

    /**
     * 关闭对话框
     */
    fun stopDialog() {
        if (mDialog != null && mDialog!!.isShowing) {
            mDialog?.dismiss()
        }
    }

    /**
     * 跳转activity
     */
    fun jumpActivity(context: Context, activityClass: Class<*>?,
                     bundle: Bundle? = null, finish: Boolean = false) {
        //希望都能使用如下的方式进行页面跳转 统一处理后期比如有埋点的操作
        PageJumpHelper.jumpActivity(context, activityClass, bundle, finish)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mActivity = this
    }

    override fun getActivityInstance(): RootActivity<*> {
        return this
    }

    override fun onDestroy() {
        KeyboardUtils.fixSoftInputLeaks(this)
        super.onDestroy()
        //如果需要eventbus
        if (useEventBus()) {
            EventBus.getDefault().unregister(this)
        }
        mActivity = null
    }

    override fun finish() {
        super.finish()
        // 在这里统一加了关闭键盘的逻辑，防止用户点击左上角返回键而键盘没有关闭
        KeyboardUtils.hideSoftInput(this)
    }

    /**
     * 是否需要帮你默认初始化状态栏
     */
    open fun useStatusBarInit(): Boolean = true

    /**
     * 是否启用EventBus
     */
    open fun useEventBus(): Boolean = false

    /**
     * 是否要使用通用的头部
     */
    open fun useCommonHeader(): Boolean = false

    /**
     * 通用显示toast的地方
     */
    fun showToast(msg: String?) {
        ToastHelper.show(msg)
    }

    override fun getResources(): Resources {
        // 避免偶尔屏幕适配失效
        // https://github.com/JessYanCoding/AndroidAutoSize/issues/299
        if (Looper.myLooper() == Looper.getMainLooper() && this !is CancelAdapt) {
            AutoSizeCompat.autoConvertDensityOfGlobal((super.getResources()))
        }
        return super.getResources()
    }

    /**
     * 绑定头部的点击事件
     */
    fun bindHeaderViewClickListener(listener: (id: Int, view: View) -> Unit) {
        headerViewContainer?.bindClickListener { id, view ->
            listener(id, view)
        }
    }

    open fun initListener() {

    }
}