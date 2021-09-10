package com.roll.codemao.base.ui

import android.app.Activity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import com.blankj.utilcode.util.BarUtils
import com.roll.codemao.R
import com.roll.codemao.app.data.api.model.event.NotifyNetStateChange
import com.roll.codemao.base.ui.expand.qmui.createTipDialog
import com.roll.codemao.view.loadlayout.LoadLayout
import com.roll.codemao.view.loadlayout.State
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import com.roll.codemao.base.ui.helper.HeaderViewHelper
import com.roll.codemao.utils.ToastHelper
import com.roll.codemao.view.HeaderViewContainer
import com.trello.rxlifecycle2.components.support.RxFragment
import org.greenrobot.eventbus.EventBus

/**
 * Created by cretin on 16/10/27.
 */
abstract class RootFragment<RB : ViewDataBinding?> : RxFragment() {
    protected var mActivity: Activity? = null
    private var rootView: View? = null

    var headerViewHelper: HeaderViewHelper? = null
    private var headerViewContainer: HeaderViewContainer? = null

    //当前页面的对话框
    private var mDialog: QMUITipDialog? = null
    private var mBinding: RB? = null
    private var mLoadLayout: LoadLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (useEventBus())
            EventBus.getDefault().register(this)
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mActivity = activity
    }

    override fun onDestroy() {
        super.onDestroy()
        if (useEventBus())
            EventBus.getDefault().unregister(this)
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

    //是否需要自动刷新 如果需要 返回true即可
    fun autoRefresh(): Boolean {
        return false
    }

    /**
     * 显示加载对话框
     *
     * @param msg
     */
    fun showDialog(msg: String? = "", cancelable: Boolean? = false) {
        if (mActivity is RootActivity<*>) {
            (mActivity as RootActivity<*>)?.showDialog(msg, cancelable)
        } else {
            creataDialog(msg ?: "")
            mDialog?.setCancelable(cancelable == true)
            mDialog?.show()
        }
    }

    internal fun creataDialog(msg: String?): QMUITipDialog? {
        mDialog = mActivity?.createTipDialog(msg ?: "", QMUITipDialog.Builder.ICON_TYPE_LOADING)
        return mDialog
    }

    fun mBinding(): RB {
        if (mBinding == null) {
            createBinding()
        }
        return mBinding!!
    }

    /**
     * 通用显示toast的地方
     */
    fun showToast(msg: String?) {
        ToastHelper.show(msg)
    }

    /**
     * 关闭对话框
     */
    fun stopDialog() {
        if (mDialog != null && mDialog!!.isShowing) {
            mDialog?.dismiss()
        }
    }


    override fun onResume() {
        super.onResume()
        if (autoRefresh()) {
            refreshPageData()
        }
    }

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.layout_base, null)
        //自己消费点击事件 防止事件穿透
        rootView?.setOnTouchListener(OnTouchListener { v: View?, event: MotionEvent? -> true })
        initContentView(rootView, container, savedInstanceState)
        initListener()
        return rootView
    }

    private fun initContentView(view: View?, containerRoot: ViewGroup?, savedInstanceState: Bundle?) {
        mLoadLayout = view!!.findViewById<View>(R.id.ll_base) as LoadLayout
        //设置页面为“加载”状态
        mLoadLayout?.layoutState = State.LOADING
        if (contentViewId != 0) {
            createBinding()
            if (mBinding == null) {
                throw RuntimeException("布局文件需要支持DataBinding")
            }

            //在这里要处理下通用头部的逻辑
            if (useCommonHeader()) {
                headerViewHelper = HeaderViewHelper()
                headerViewContainer = HeaderViewContainer(mActivity!!)
                headerViewContainer?.setContentView(mBinding!!.root)
                headerViewHelper?.bindHeaderView(headerViewContainer!!)

                if (mActivity is RootActivity<*>) {
                    if ((mActivity as RootActivity<*>).isKitkat) {
                        headerViewContainer?.setHeaderPaddingTop(BarUtils.getStatusBarHeight())
                    }
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
        mLoadLayout?.setLoadFailedRetryListener {
            showProgressView()
            refreshPageData()
        }
    }

    protected open fun createBinding() {
        mBinding = DataBindingUtil.inflate<RB>(LayoutInflater.from(mActivity), contentViewId, null, false)
    }

    //展示主视图
    fun showContentView() {
        //隐藏掉整个加载中和错误页面
        mLoadLayout!!.layoutState = State.SUCCESS
    }

    //显示正在加载视图
    fun showProgressView() {
        mLoadLayout!!.layoutState = State.LOADING
    }

    //显示加载错误
    fun showErrorView() {
        mLoadLayout!!.layoutState = State.FAILED
    }

    protected abstract val contentViewId: Int

    protected abstract fun initView(v: View?, savedInstanceState: Bundle?)

    /**
     * 将整个页面需要在刷新时加载的数据操作都放在这个里面 当出现无网络界面
     * 点击屏幕刷新数据的时候会调用里面的方法 在数据加载完请记得调用showContent方法
     */
    protected abstract fun refreshPageData()

    open fun initListener() {}

    /**
     * 是否启用EventBus
     */
    open fun useEventBus(): Boolean = false

    /**
     * 绑定头部的点击事件
     */
    fun bindHeaderViewClickListener(listener: (id: Int, view: View) -> Unit) {
        headerViewContainer?.bindClickListener { id, view ->
            listener(id, view)
        }
    }


    /**
     * 是否要使用通用的头部
     */
    open fun useCommonHeader(): Boolean = false
}