package com.roll.codemao.ui

import android.Manifest
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.SpanUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.cretin.tools.fanpermission.FanPermissionListener
import com.cretin.tools.fanpermission.FanPermissionUtils
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.interfaces.OnConfirmListener
import com.roll.codemao.R
import com.roll.codemao.app.data.api.model.event.NotifyUploadSuccess
import com.roll.codemao.app_server.*
import com.roll.codemao.app_server.org.nanohttpd.protocols.http.progress.ProgressListener
import com.roll.codemao.app_server.org.nanohttpd.protocols.http.request.Method
import com.roll.codemao.base.ui.BaseActivity
import com.roll.codemao.base.ui.expand.glide.GlideHelper
import com.roll.codemao.databinding.ActivityServerConnectBinding
import com.roll.codemao.utils.ResUtils
import com.roll.codemao.utils.RxUtils
import com.roll.www.zhulishitidian.utils.expand.setOnClick
import io.reactivex.Observable
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.joda.time.DateTime
import org.json.JSONException
import java.io.File

class ServerConnectActivity : BaseActivity<ActivityServerConnectBinding>(), ProgressListener {

    private var mHttpServer: HttpServer? = null
    private var mBytesRead: Long = 0
    private var mContentLength: Long = 0
    private var currentState = false
    private var list: MutableList<MutableMap<String, Any>>? = null
    private var adapter: RecyclerAdapter? = null

    override fun useCommonHeader(): Boolean {
        return true
    }

    override fun update(pBytesRead: Long, pContentLength: Long) {
        mBytesRead = pBytesRead
        mContentLength = pContentLength
    }

    override fun getBytesRead(): Long {
        return mBytesRead
    }

    override fun getContentLength(): Long {
        return mContentLength
    }

    override fun initView(view: View?, savedInstanceState: Bundle?) {
        showContentView()
        headerViewHelper?.setPageTitle("WIFI??????Apk")

        checkoutPermission()

        mBinding().tvBtn.setOnClick {
            currentState = !currentState
            if (currentState) {
                mHttpServer?.start()

                mBinding().tvBtn.text = "WIFI?????????????????????"
                mBinding().tvTips.text = "????????????????????????????????????" +
                        "http://${mHttpServer!!.getHostname()}:${mHttpServer!!.listeningPort}"
            } else {
                mBinding().tvTips.text = "????????????WIFI??????APK???????????????????????????"
                mBinding().tvBtn.text = "??????WIFI??????????????????"

                mHttpServer?.stop()
            }
        }

        mBinding().swipeRefresh.setColorSchemeColors(ResUtils.getColor(R.color.orange))
        mBinding().swipeRefresh.setOnRefreshListener {
            getMainData()
        }
    }

    override val contentViewId = R.layout.activity_server_connect

    override fun refreshPageData() {
        getMainData()
    }

    private fun checkoutPermission() {
        FanPermissionUtils.with(this) //????????????????????????????????????
                .addPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE) //?????????????????????????????? ?????????????????? ?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                .setPermissionsCheckListener(object : FanPermissionListener {
                    override fun permissionRequestSuccess() {
                        getMainData()
                    }

                    override fun permissionRequestFail(grantedPermissions: Array<String>, deniedPermissions: Array<String>, forceDeniedPermissions: Array<String>) {}
                }) //????????????
                .createConfig() //????????????????????????????????????????????????????????????true??????????????????????????????????????????????????????????????????????????????
                .setForceAllPermissionsGranted(true) //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                .setForceDeniedPermissionTips("???????????????->??????->???" + FanPermissionUtils.getAppName(this) + "???->???????????????????????????????????????????????????????????????") //?????????????????????
                .buildConfig() //????????????
                .startCheckPermission()
    }

    private fun getMainData() {
        Observable.just(0)
                .map {
                    val tempList = mutableListOf<MutableMap<String, Any>>()
                    val file = File(Environment.getExternalStorageDirectory().toString() + File.separator + UploadFileDispatcher.DIR_IN_SDCARD + File.separator + AppUtils.getAppPackageName())
                    if (file.exists()) {
                        val files = FileUtils.listFilesInDir(file)
                        files.sortBy { it.lastModified() }
                        for (file1 in files) {
                            try {
                                val apkFile = AppUtils.getApkInfo(file1)
                                val temp = mutableMapOf<String, Any>()
                                temp["name"] = apkFile.name
                                temp["size"] = ConvertUtils.byte2FitMemorySize(file1.length(), 2)
                                temp["time"] = DateTime(file1.lastModified()).toString("yyyy-MM-dd HH:mm:ss")
                                temp["icon"] = apkFile.icon
                                temp["version"] = apkFile.versionName
                                temp["packageName"] = apkFile.packageName
                                temp["isInstall"] = AppUtils.isAppInstalled(apkFile.packageName)
                                temp["fileUrl"] = file1.path
                                temp["fileName"] = file1.name
                                tempList.add(temp)
                            } catch (e: Throwable) {
                                e.printStackTrace()
                            }
                        }
                    }
                    tempList
                }.compose(RxUtils.applySchedulers())
                .subscribe({
                    list?.clear()
                    list?.addAll(it)
                    adapter?.notifyDataSetChanged()
                    adapter?.loadMoreEnd()
                    mBinding().swipeRefresh.isRefreshing = false
                }, {
                    mBinding().swipeRefresh.isRefreshing = false
                })
    }

    private fun initAdapter() {
        list = mutableListOf()
        mBinding().recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RecyclerAdapter(list ?: mutableListOf())
        adapter?.setOnLoadMoreListener({ }, mBinding().recyclerView)
        adapter?.setEmptyView(R.layout.empty_view, mBinding().recyclerView)
        adapter?.emptyView?.findViewById<TextView>(R.id.tv_tips)?.text = "?????????????????????APP"
        mBinding().recyclerView.adapter = adapter

        adapter?.setOnItemClickListener { adapter, view, position ->

        }
    }

    override fun useEventBus(): Boolean {
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        mHttpServer?.stop()
        currentState = false
        mBinding().tvTips.text = "????????????WIFI??????APK???????????????????????????"
        mBinding().tvBtn.text = "??????WIFI??????????????????"
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun event(event: NotifyUploadSuccess) {
        getMainData()
    }

    override fun onResume() {
        super.onResume()
        getMainData()
    }

    inner class RecyclerAdapter(datas: MutableList<MutableMap<String, Any>>) :
            BaseQuickAdapter<MutableMap<String, Any>, BaseViewHolder>(R.layout.item_recyclerview_app_home_top, datas) {
        override fun convert(helper: BaseViewHolder?, item: MutableMap<String, Any>) {
            //????????????
            GlideHelper.loadRoundImage(this@ServerConnectActivity, helper?.getView<ImageView>(R.id.iv_icon)!!, item["icon"] as Drawable)
            helper?.setText(R.id.tv_title, item["name"].toString())
            val tips = (if (item["isInstall"].toString().toBoolean()) "\n???????????????????????????????????????????????????????????????" else "")
            val span = SpanUtils().append("???????????????" + item["packageName"] + "\n????????????" + item["fileName"].toString())
                    .append(tips)
                    .setForegroundColor(ResUtils.getColor(R.color.red_ef0000))
                    .create()
            helper?.setText(R.id.tv_desc, span)
            helper?.setText(R.id.tv_time, item["time"].toString() + " ??????      " + item["size"])

            helper?.getView<TextView>(R.id.tv_btn)?.apply {
                setBackgroundResource(R.drawable.selecter_bg_round_btn)
                visibility = View.VISIBLE
                setOnClickListener {
                    AppUtils.installApp(File(item["fileUrl"].toString()))
                }
            }

            helper?.getView<TextView>(R.id.tv_flag)?.apply {
                text = if (item["version"].toString().contains("v")) item["version"].toString() else "V" + item["version"].toString()
                //Android
                setCompoundDrawablesWithIntrinsicBounds(ResUtils.getDrawable(R.mipmap.android_icon), null, null, null)
            }

            helper?.getView<TextView>(R.id.tv_delete)?.apply {
                visibility = View.VISIBLE
                setOnClick {
                    XPopup.Builder(this@ServerConnectActivity)
                            .dismissOnBackPressed(true)
                            .dismissOnTouchOutside(true)
                            .isDestroyOnDismiss(true)
                            .asConfirm("??????", "????????????????????????????????????????????????",
                                    "??????", "??????",
                                    OnConfirmListener {
                                        val success = FileUtils.delete(File(item["fileUrl"].toString()))
                                        if (success) {
                                            getMainData()
                                        }
                                    }, null, false)
                            .show()
                }
            }
        }
    }

    override fun initData() {
        mHttpServer = HttpServer(50000, this)
        mHttpServer?.register(Method.GET, "/", ResourceDispatcher(this))
                ?.register(Method.GET, "/images/.*", ResourceDispatcher(this))
                ?.register(Method.GET, "/scripts/.*", ResourceDispatcher(this))
                ?.register(Method.GET, "/css/.*", ResourceDispatcher(this))
                ?.register(Method.GET, "/imgs/.*", ResourceDispatcher(this))
                ?.register(Method.GET, "/js/.*", ResourceDispatcher(this))
                ?.register(Method.GET, "/getDeviceInfo", DeviceInfoDispatcher(this))
                ?.register(Method.POST, "/upload", UploadFileDispatcher())
                ?.register(Method.GET, "/upload", UploadFileProgressDispathcer(this))
                ?.register(Method.POST, "/longpolling", LongPollingDispatcher())
                ?.register(Method.GET, "/appList", AppListDispatcher(this))
        initAdapter()
    }
}