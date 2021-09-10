package com.roll.codemao.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cretin.tools.androidutils.openapp.OpenAppUtils
import com.roll.codemao.R
import com.roll.codemao.app.KV
import com.roll.codemao.app.LocalStorageKeys
import com.roll.codemao.app.data.api.ApiConfig
import com.roll.codemao.base.ui.BaseActivity
import com.roll.codemao.base.ui.RootActivity
import com.roll.codemao.base.ui.expand.glide.GlideHelper
import com.roll.codemao.base.ui.helper.PageJumpHelper
import com.roll.codemao.databinding.LayoutRecyclerviewWithRefreshBinding
import com.roll.codemao.model.resp.HomeAppListResp
import com.roll.codemao.utils.FileUtils
import com.roll.codemao.utils.ResUtils
import kotlinx.android.synthetic.main.activity_bind_token.*

class AppListActivity : BaseActivity<LayoutRecyclerviewWithRefreshBinding>() {

    private var currentPage = 1
    private var list: MutableList<HomeAppListResp>? = null
    private var adapter: RecyclerAdapter? = null

    private var appKey = ""

    companion object {
        fun start(context: Context, appKey: String, buildKey: String, appName: String, buildPassword: String) {
            val bundle = Bundle()
            bundle.putString("appKey", appKey)
            bundle.putString("buildKey", buildKey)
            bundle.putString("name", appName)
            bundle.putString("buildPassword", buildPassword)
            PageJumpHelper.jumpActivity(context, AppListActivity::class.java, bundle)
        }
    }

    override fun initData() {
        getData()
    }

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        headerViewHelper?.setPageTitle(intent?.getStringExtra("name") ?: "")

        mBinding().swipeRefresh.setColorSchemeColors(ResUtils.getColor(R.color.orange))
        mBinding().swipeRefresh.setOnRefreshListener {
            currentPage = 1
            getData()
        }

        initAdapter()
    }

    private fun getData() {
        appKey = KV.get<String>(LocalStorageKeys.LOCAL_API_KEY)
        if (TextUtils.isEmpty(appKey)) {
            showToast("请先绑定API_KEY")
            return
        }
        val map = mutableMapOf<String, String>()
        map["appKey"] = intent?.getStringExtra("appKey") ?: ""
        map["buildKey"] = intent?.getStringExtra("buildKey") ?: ""
        map["_api_key"] = appKey
        map["page"] = currentPage.toString()
        httpHelper?.doNetWork(apiService?.getHomeAppVersionList(map), success = {
            if (currentPage == 1) {
                list?.clear()
            }
            list?.addAll(it.data.list)
            if (it.data.list.size < 20) {
                adapter?.loadMoreEnd()
            } else {
                adapter?.loadMoreComplete()
            }
            adapter?.notifyDataSetChanged()
            currentPage++
        }, showDialog = false, fail = { msg, isErr ->
            adapter?.loadMoreEnd()
        }, complete = {
            mBinding().swipeRefresh.isRefreshing = false
        })
    }

    override fun useCommonHeader(): Boolean {
        return true
    }

    override val contentViewId: Int
        get() = R.layout.layout_recyclerview_with_refresh

    override fun refreshPageData() {}

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

        }
    }

    inner class RecyclerAdapter(datas: MutableList<HomeAppListResp>) :
            BaseQuickAdapter<HomeAppListResp, BaseViewHolder>(R.layout.item_recyclerview_app_home, datas) {
        override fun convert(helper: BaseViewHolder?, item: HomeAppListResp) {
            //获取链接
            val url = StringBuilder("https://cdn-app-icon.pgyer.com")
            item.buildIcon.substring(0, 5).forEach {
                url.append("/$it")
            }
            url.append("/" + item.buildIcon + "?x-oss-process=image/resize,m_lfit,h_120,w_120/format,jpg")

            GlideHelper.loadRoundImage(this@AppListActivity, helper?.getView<ImageView>(R.id.iv_icon)!!, url.toString())
            helper?.setText(R.id.tv_title, item.buildName)
            helper?.setText(R.id.tv_desc, "更新说明：(第" + item.buildBuildVersion + "次打包)\n" + item.buildUpdateDescription)
            helper?.setText(R.id.tv_time, item.buildCreated + " 更新      " + FileUtils.getPrintSize(item.buildFileSize.toLong()))

            helper?.getView<TextView>(R.id.tv_btn)?.apply {
                if (item.buildType == "1") {
                    setBackgroundResource(R.drawable.selecter_bg_round_btn_enable)
                } else {
                    setBackgroundResource(R.drawable.selecter_bg_round_btn)
                }
                visibility = View.VISIBLE
                setOnClickListener {
                    if (item.buildType == "1") {
                        showToast("当前设备仅支持安装安卓应用")
                        return@setOnClickListener
                    }
                    val buildKey = item.buildKey
                    val buildPassword = intent?.getStringExtra("buildPassword") ?: ""
                    val url = "https://www.pgyer.com/apiv2/app/install?_api_key=" + appKey + "&buildKey=" + buildKey + "&buildPassword=" + buildPassword
                    OpenAppUtils.openPhoneBrowser(this@AppListActivity, url)
                }
            }

            helper?.getView<TextView>(R.id.tv_flag)?.apply {
                text = if (item.buildVersion.contains("v")) item.buildVersion else "V" + item.buildVersion
                if (item.buildType == "1") {
                    //iOS
                    setCompoundDrawablesWithIntrinsicBounds(ResUtils.getDrawable(R.mipmap.ios_icon), null, null, null)
                } else {
                    //Android
                    setCompoundDrawablesWithIntrinsicBounds(ResUtils.getDrawable(R.mipmap.android_icon), null, null, null)
                }
            }
        }
    }

}