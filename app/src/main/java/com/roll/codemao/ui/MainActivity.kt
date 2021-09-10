package com.roll.codemao.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.OnConfirmListener
import com.roll.codemao.R
import com.roll.codemao.app.KV
import com.roll.codemao.app.LocalStorageKeys
import com.roll.codemao.app.data.api.ApiConfig
import com.roll.codemao.app.data.api.model.event.NotifyTokenBind
import com.roll.codemao.base.ui.BaseActivity
import com.roll.codemao.base.ui.expand.glide.GlideHelper
import com.roll.codemao.base.ui.expand.kotlin.io_
import com.roll.codemao.base.ui.helper.PageJumpHelper
import com.roll.codemao.databinding.LayoutRecyclerviewWithRefreshBinding
import com.roll.codemao.model.resp.HomeAppListResp
import com.roll.codemao.model.resp.HomeAppListRootResp
import com.roll.codemao.utils.FileUtils
import com.roll.codemao.utils.ResUtils
import com.roll.codemao.utils.RxUtils
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import org.greenrobot.eventbus.Subscribe
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.io.IOException

class MainActivity : BaseActivity<LayoutRecyclerviewWithRefreshBinding>() {
    private var list: MutableList<HomeAppListResp>? = null
    private var adapter: RecyclerAdapter? = null

    override fun initData() {
        getData()
    }

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        headerViewHelper?.setPageTitle("所有项目")
        headerViewHelper?.headerViewContainer?.getBackBtn()?.visibility = View.GONE
        headerViewHelper?.headerViewContainer?.getRightTextView()?.text = "设置"
        headerViewHelper?.headerViewContainer?.getRightTextView()?.visibility = View.VISIBLE
        headerViewHelper?.headerViewContainer?.getRightTextView()?.setOnClickListener {
            val platFormTips = when (KV.get<Int>(LocalStorageKeys.HOME_PLATFORM_TYPE, 0)) {
                0 -> {
                    "所有版本"
                }
                1 -> {
                    "只看iOS"
                }
                2 -> {
                    "只看安卓"
                }
                else -> {
                    "所有版本"
                }
            }
            XPopup.Builder(this)
                .hasShadowBg(false)
                .atView(it) // 依附于所点击的View，内部会自动判断在上方或者下方显示
                .asAttachList(
                    arrayOf("绑定ApiKey", "置顶应用", platFormTips, "WIFI安装APK"),
                    null,  //                                new int[]{R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round},
                    { position, text ->
                        if (position == 0) {
                            PageJumpHelper.jumpActivity(this, BindTokenActivity::class.java)
                        } else if (position == 2) {
                            XPopup.Builder(this)
                                .isDarkTheme(false)
                                .hasShadowBg(true) //                            .hasBlurBg(true)
                                //                            .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                                .asBottomList(
                                    "请选择", arrayOf("所有版本", "只看安卓", "只看iOS")
                                ) { position, text ->
                                    showToast("设置成功")
                                    if (position == 0) {
                                        KV.put(LocalStorageKeys.HOME_PLATFORM_TYPE, 0)
                                        getData()
                                    } else if (position == 1) {
                                        KV.put(LocalStorageKeys.HOME_PLATFORM_TYPE, 2)
                                        getData()
                                    } else {
                                        KV.put(LocalStorageKeys.HOME_PLATFORM_TYPE, 1)
                                        getData()
                                    }
                                }.show()
                        } else if (position == 1) {
                            //置顶
                            jumpActivity(this, TopActivity::class.java)
                        } else if (position == 3) {
                            jumpActivity(this, ServerConnectActivity::class.java)
                        }
                    }, 0, 0 /*, Gravity.LEFT*/
                )
                .show()
        }

        mBinding().swipeRefresh.setColorSchemeColors(ResUtils.getColor(R.color.orange))
        mBinding().swipeRefresh.setOnRefreshListener {
            getData()
        }

        initAdapter()
    }

    @SuppressLint("CheckResult")
    private fun getData() {
        val api = KV.get<String>(LocalStorageKeys.LOCAL_API_KEY, "") ?: ""
        if (TextUtils.isEmpty(api)) {
            showContentView()
            mBinding().swipeRefresh.isRefreshing = false

            //没有新版本
            val showTips = KV.get<Boolean>(LocalStorageKeys.HOME_SHOW_TIPS, false)
                ?: false
            if (!showTips) {
                XPopup.Builder(this)
                    .dismissOnBackPressed(true)
                    .dismissOnTouchOutside(true)
                    .isDestroyOnDismiss(true)
                    .asConfirm(
                        "提示", "如需正常使用功能，需要绑定蒲公英的ApiKey，如需替换请点击右上角设置绑定你自己项目的ApiKey",
                        "取消", "现在绑定",
                        OnConfirmListener {
                            jumpActivity(this, BindTokenActivity::class.java)
                        }, null, false
                    )
                    .show()
                KV.put(LocalStorageKeys.HOME_SHOW_TIPS, true)
            }else{
                showToast("请先绑定API_KEY")
            }

            return
        }

        Observable.zip(
            apiService?.getHomeAppList(api, 1)?.io_()?.onErrorReturn {
                com.roll.codemao.app.data.api.model.Result()
            },
            Observable.zip(
                apiService?.getHomeAppList(api, 2)?.io_()?.onErrorReturn {
                    com.roll.codemao.app.data.api.model.Result()
                },
                apiService?.getHomeAppList(api, 3)?.io_()?.onErrorReturn {
                    com.roll.codemao.app.data.api.model.Result()
                },
                BiFunction<com.roll.codemao.app.data.api.model.Result<HomeAppListRootResp>, com.roll.codemao.app.data.api.model.Result<HomeAppListRootResp>, HomeAppListRootResp> { t1, t2 ->
                    val result = HomeAppListRootResp()
                    result.list = mutableListOf()
                    if (t1?.data?.list?.isEmpty() == false) {
                        result.list.addAll(t1?.data?.list ?: mutableListOf())
                    }
                    if (t2?.data?.list?.isEmpty() == false) {
                        result.list.addAll(t2?.data?.list ?: mutableListOf())
                    }
                    result
                }),
            BiFunction<com.roll.codemao.app.data.api.model.Result<HomeAppListRootResp>, HomeAppListRootResp, HomeAppListRootResp> { t1, t2 ->
                val result = HomeAppListRootResp()
                result.list = mutableListOf()
                val tempList = mutableListOf<HomeAppListResp>()
                val resultListTop = mutableListOf<HomeAppListResp>()
                val resultListNormal = mutableListOf<HomeAppListResp>()
                if (t1?.data?.list?.isEmpty() == false) {
                    tempList.addAll(t1?.data?.list ?: mutableListOf())
                }
                if (t2?.list?.isEmpty() == false) {
                    tempList.addAll(t2?.list ?: mutableListOf())
                }
                val topApps = KV.get<String>(LocalStorageKeys.HOME_TOP_APP_ID, "")
                val platForm = KV.get<Int>(LocalStorageKeys.HOME_PLATFORM_TYPE, 0)
                tempList.forEach {
                    it.isSelect = topApps.contains(it.buildKey)
                    it.buildCreatedTime = DateTime.parse(
                        it.buildCreated,
                        DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
                    ).millis
                    //将需要的类型添加减去
                    if (it.isSelect) {
                        if (platForm == 1) {
                            //ios
                            if (it.buildType == "1")
                                resultListTop.add(it)
                        } else if (platForm == 2) {
                            //Android
                            if (it.buildType == "2")
                                resultListTop.add(it)
                        } else {
                            //都要
                            resultListTop.add(it)
                        }
                    } else {
                        if (platForm == 1) {
                            //ios
                            if (it.buildType == "1")
                                resultListNormal.add(it)
                        } else if (platForm == 2) {
                            //Android
                            if (it.buildType == "2")
                                resultListNormal.add(it)
                        } else {
                            //都要
                            resultListNormal.add(it)
                        }
                    }
                }
                resultListTop.sortBy { -it.buildCreatedTime }
                resultListNormal.sortBy { -it.buildCreatedTime }
                result.list.addAll(resultListTop)
                result.list.addAll(resultListNormal)
                result
            })?.compose(RxUtils.applySchedulers())?.subscribe({
            list?.clear()
            list?.addAll(it.list)
            adapter?.loadMoreEnd()
            adapter?.notifyDataSetChanged()
            mBinding().swipeRefresh.isRefreshing = false
            showContentView()
        }, {
            adapter?.loadMoreComplete()
            adapter?.notifyDataSetChanged()
            mBinding().swipeRefresh.isRefreshing = false
            showContentView()
        })
    }

    override fun useCommonHeader(): Boolean {
        return true
    }

    override fun useEventBus(): Boolean {
        return true
    }

    @Subscribe
    fun event(event: NotifyTokenBind) {
        list?.clear()
        adapter?.notifyDataSetChanged()
        getData()
    }

    override val contentViewId: Int
        get() = R.layout.layout_recyclerview_with_refresh

    override fun refreshPageData() {
        getData()
    }

    private fun initAdapter() {
        list = mutableListOf()
        mBinding().recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RecyclerAdapter(list ?: mutableListOf())
        mBinding().recyclerView.adapter = adapter
        adapter?.setEmptyView(R.layout.empty_view, mBinding().recyclerView)
        adapter?.setOnLoadMoreListener({
            getData()
        }, mBinding().recyclerView)

        adapter?.setOnItemClickListener { adapter, view, position ->
            AppListActivity.start(
                this, list?.get(position)?.appKey
                    ?: "", list?.get(position)?.buildKey ?: "", list?.get(position)?.buildName
                    ?: "", list?.get(position)?.buildPassword ?: ""
            )
        }
    }

    inner class RecyclerAdapter(datas: MutableList<HomeAppListResp>) :
        BaseQuickAdapter<HomeAppListResp, BaseViewHolder>(
            R.layout.item_recyclerview_app_home,
            datas
        ) {
        override fun convert(helper: BaseViewHolder?, item: HomeAppListResp) {
            //获取链接
            val url = StringBuilder("https://cdn-app-icon.pgyer.com")
            item.buildIcon.substring(0, 5).forEach {
                url.append("/$it")
            }
            url.append("/" + item.buildIcon + "?x-oss-process=image/resize,m_lfit,h_120,w_120/format,jpg")

            GlideHelper.loadRoundImage(
                this@MainActivity,
                helper?.getView<ImageView>(R.id.iv_icon)!!,
                url.toString()
            )
            helper?.setText(R.id.tv_title, item.buildName)
            helper?.setText(
                R.id.tv_desc,
                "更新说明：" + "(第" + item.buildBuildVersion + "次打包)" + (if (item.isSelect) "【已置顶】" else "") + "\n" + item.buildUpdateDescription
            )
            helper?.setText(
                R.id.tv_time,
                item.buildCreated + " 更新      " + FileUtils.getPrintSize(item.buildFileSize.toLong())
            )

            helper?.getView<TextView>(R.id.tv_flag)?.apply {
                text =
                    if (item.buildVersion.contains("v")) item.buildVersion else "V" + item.buildVersion
                if (item.buildType == "1") {
                    //iOS
                    setCompoundDrawablesWithIntrinsicBounds(
                        ResUtils.getDrawable(R.mipmap.ios_icon),
                        null,
                        null,
                        null
                    )
                } else {
                    //Android
                    setCompoundDrawablesWithIntrinsicBounds(
                        ResUtils.getDrawable(R.mipmap.android_icon),
                        null,
                        null,
                        null
                    )
                }
            }
        }
    }
}