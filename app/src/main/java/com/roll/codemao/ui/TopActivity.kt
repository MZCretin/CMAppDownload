package com.roll.codemao.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lxj.xpopup.XPopup
import com.roll.codemao.R
import com.roll.codemao.app.KV
import com.roll.codemao.app.LocalStorageKeys
import com.roll.codemao.app.data.api.ApiConfig
import com.roll.codemao.app.data.api.model.Result
import com.roll.codemao.app.data.api.model.event.NotifyLoadMore
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
import org.greenrobot.eventbus.EventBus
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class TopActivity : BaseActivity<LayoutRecyclerviewWithRefreshBinding>() {
    private var list: MutableList<HomeAppListResp>? = null
    private var adapter: RecyclerAdapter? = null

    override fun useCommonHeader(): Boolean {
        return true
    }

    override fun initData() {
        getData()
    }

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        headerViewHelper?.setPageTitle("置顶项目")
        headerViewHelper?.headerViewContainer?.getBackBtn()?.visibility = View.GONE
        headerViewHelper?.headerViewContainer?.getRightTextView()?.text = "保存"
        headerViewHelper?.headerViewContainer?.getRightTextView()?.visibility = View.VISIBLE
        headerViewHelper?.headerViewContainer?.getRightTextView()?.setOnClickListener {
            var result = list?.joinToString(separator = ",") { if (it.isSelect) it.buildKey else "####" }?.replace("####,", "")?.replace("####", "")
            if (result?.endsWith(",") == true) {
                result = result.substring(0, result.length - 1)
            }
            KV.put(LocalStorageKeys.HOME_TOP_APP_ID, result)
            showToast("保存成功")

            EventBus.getDefault().post(NotifyTokenBind())

            finish()
        }

        mBinding().swipeRefresh.setColorSchemeColors(ResUtils.getColor(R.color.orange))
        mBinding().swipeRefresh.setOnRefreshListener {
            getData()
        }

        initAdapter()
    }

    override val contentViewId = R.layout.layout_recyclerview_with_refresh

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
            val item = list?.get(position)
            if (item != null) {
                item.isSelect = !item.isSelect
                adapter?.notifyItemChanged(position)
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun getData() {
        var api = KV.get<String>(LocalStorageKeys.LOCAL_API_KEY, "") ?: ""
        if (TextUtils.isEmpty(api)) {
            showToast("请先绑定API_KEY")
            return
        }

        Observable.zip(apiService?.getHomeAppList(api, 1)?.io_()?.onErrorReturn {
            com.roll.codemao.app.data.api.model.Result()
        }, Observable.zip(apiService?.getHomeAppList(api, 2)?.io_()?.onErrorReturn {
            com.roll.codemao.app.data.api.model.Result()
        }, apiService?.getHomeAppList(api, 3)?.io_()?.onErrorReturn {
            com.roll.codemao.app.data.api.model.Result()
        }, BiFunction<Result<HomeAppListRootResp>, Result<HomeAppListRootResp>, HomeAppListRootResp> { t1, t2 ->
            val result = HomeAppListRootResp()
            result.list = mutableListOf()
            if (t1?.data?.list?.isEmpty() == false) {
                result.list.addAll(t1?.data?.list ?: mutableListOf())
            }
            if (t2?.data?.list?.isEmpty() == false) {
                result.list.addAll(t2?.data?.list ?: mutableListOf())
            }
            result
        }), BiFunction<com.roll.codemao.app.data.api.model.Result<HomeAppListRootResp>, HomeAppListRootResp, HomeAppListRootResp> { t1, t2 ->
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
            tempList.forEach {
                it.isSelect = topApps.contains(it.buildKey)
                it.buildCreatedTime = DateTime.parse(it.buildCreated, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")).millis
                //将需要的类型添加减去
                if(it.isSelect){
                    resultListTop.add(it)
                }else{
                    resultListNormal.add(it)
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

    inner class RecyclerAdapter(datas: MutableList<HomeAppListResp>) :
            BaseQuickAdapter<HomeAppListResp, BaseViewHolder>(R.layout.item_recyclerview_app_home_top, datas) {
        override fun convert(helper: BaseViewHolder?, item: HomeAppListResp) {
            //获取链接
            val url = StringBuilder("https://cdn-app-icon.pgyer.com")
            item.buildIcon.substring(0, 5).forEach {
                url.append("/$it")
            }
            url.append("/" + item.buildIcon + "?x-oss-process=image/resize,m_lfit,h_120,w_120/format,jpg")

            GlideHelper.loadRoundImage(this@TopActivity, helper?.getView<ImageView>(R.id.iv_icon)!!, url.toString())
            helper?.setText(R.id.tv_title, item.buildName)
            helper?.setText(R.id.tv_desc, "更新说明：" + "(第" + item.buildBuildVersion + "次打包)" + "\n" + item.buildUpdateDescription)
            helper?.setText(R.id.tv_time, item.buildCreated + " 更新      " + FileUtils.getPrintSize(item.buildFileSize.toLong()))

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

            if (item.isSelect) {
                helper?.setImageResource(R.id.iv_select, R.mipmap.selected)
            } else {
                helper?.setImageResource(R.id.iv_select, R.mipmap.unselected)
            }
        }
    }

}