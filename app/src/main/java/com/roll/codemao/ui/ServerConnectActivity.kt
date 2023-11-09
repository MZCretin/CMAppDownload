package com.roll.codemao.ui

import android.Manifest
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.roll.codemao.base.ui.expand.glide.GlideHelper
import com.roll.codemao.utils.ResUtils
import com.roll.codemao.utils.RxUtils
import com.roll.www.zhulishitidian.utils.expand.setOnClick
import io.reactivex.Observable
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.joda.time.DateTime
import org.json.JSONException
import java.io.File

class ServerConnectActivity : AppCompatActivity(), ProgressListener {

    private var mHttpServer: HttpServer? = null
    private var mBytesRead: Long = 0
    private var mContentLength: Long = 0
    private var currentState = false
    private var list: MutableList<MutableMap<String, Any>>? = null
    private var adapter: RecyclerAdapter? = null

    private var tvBtn: Button? = null
    private var tvTips: TextView? = null
    private var recyclerView: RecyclerView? = null

    override fun update(pBytesRead: Long, pContentLength: Long) {
        mBytesRead = pBytesRead
        mContentLength = pContentLength
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_server_connect)
        EventBus.getDefault().register(this)
        initView(null, null)
        initData()
    }

    override fun getBytesRead(): Long {
        return mBytesRead
    }

    override fun getContentLength(): Long {
        return mContentLength
    }

    fun initView(view: View?, savedInstanceState: Bundle?) {
        checkoutPermission()
        tvBtn = findViewById(R.id.tv_btn)
        tvTips = findViewById(R.id.tv_tips)
        recyclerView = findViewById(R.id.recyclerView)

        tvBtn?.requestFocus()
        tvBtn?.setOnClick {
            currentState = !currentState
            if (currentState) {
                mHttpServer?.start()

                tvBtn?.text = "WIFI传输服务已启动"
                tvTips?.text = "请在浏览器输入以下地址：" +
                        "http://${mHttpServer!!.getHostname()}:${mHttpServer!!.listeningPort}"
            } else {
                tvTips?.text = "若要使用WIFI安装APK功能，请先启动服务"
                tvBtn?.text = "启动WIFI传输服务服务"

                mHttpServer?.stop()
            }
        }

//        mBinding().swipeRefresh.setColorSchemeColors(ResUtils.getColor(R.color.orange))
//        mBinding().swipeRefresh.setOnRefreshListener {
//            getMainData()
//        }
    }

    private fun checkoutPermission() {
        FanPermissionUtils.with(this) //添加所有你需要申请的权限
            .addPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE) //添加权限申请回调监听 如果申请失败 会返回已申请成功的权限列表，用户拒绝的权限列表和用户点击了不再提醒的永久拒绝的权限列表
            .setPermissionsCheckListener(object : FanPermissionListener {
                override fun permissionRequestSuccess() {
                    getMainData()
                }

                override fun permissionRequestFail(
                    grantedPermissions: Array<String>,
                    deniedPermissions: Array<String>,
                    forceDeniedPermissions: Array<String>
                ) {
                }
            }) //生成配置
            .createConfig() //配置是否强制用户授权才可以使用，当设置为true的时候，如果用户拒绝授权，会一直弹出授权框让用户授权
            .setForceAllPermissionsGranted(true) //配置当用户点击了不再提示的时候，会弹窗指引用户去设置页面授权，这个参数是弹窗里面的提示内容
            .setForceDeniedPermissionTips("请前往设置->应用->【" + FanPermissionUtils.getAppName(this) + "】->权限中打开相关权限，否则功能无法正常运行！") //构建配置并生效
            .buildConfig() //开始授权
            .startCheckPermission()
    }

    private fun getMainData() {
        Observable.just(0)
            .map {
                val tempList = mutableListOf<MutableMap<String, Any>>()
                val file = File(
                    Environment.getExternalStorageDirectory()
                        .toString() + File.separator + UploadFileDispatcher.DIR_IN_SDCARD + File.separator + AppUtils.getAppPackageName()
                )
                if (file.exists()) {
                    val files = FileUtils.listFilesInDir(file)
                    files.sortBy { it.lastModified() }
                    for (file1 in files) {
                        try {
                            val apkFile = AppUtils.getApkInfo(file1)
                            val temp = mutableMapOf<String, Any>()
                            temp["name"] = apkFile.name
                            temp["size"] = ConvertUtils.byte2FitMemorySize(file1.length(), 2)
                            temp["time"] =
                                DateTime(file1.lastModified()).toString("yyyy-MM-dd HH:mm:ss")
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

                tvBtn?.requestFocus()
//                    mBinding().swipeRefresh.isRefreshing = false
            }, {
//                    mBinding().swipeRefresh.isRefreshing = false
            })
    }

    private fun initAdapter() {
        list = mutableListOf()
        recyclerView?.layoutManager = LinearLayoutManager(this)
        adapter = RecyclerAdapter(list ?: mutableListOf())
        adapter?.setOnLoadMoreListener({ }, recyclerView)
        adapter?.setEmptyView(R.layout.empty_view, recyclerView)
        adapter?.emptyView?.findViewById<TextView>(R.id.tv_tips)?.text = "您还没有上传过APP"
        recyclerView?.adapter = adapter

        adapter?.setOnItemClickListener { adapter, view, position ->

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mHttpServer?.stop()
        currentState = false
        tvTips?.text = "若要使用WIFI安装APK功能，请先启动服务"
        tvBtn?.text = "启动WIFI传输服务服务"
        EventBus.getDefault().unregister(this)
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
        BaseQuickAdapter<MutableMap<String, Any>, BaseViewHolder>(
            R.layout.item_recyclerview_app_home_top,
            datas
        ) {
        override fun convert(helper: BaseViewHolder?, item: MutableMap<String, Any>) {
            helper?.itemView?.apply {
                setOnClick {
                    AppUtils.installApp(File(item["fileUrl"].toString()))
                }
                setOnLongClickListener {
                    AlertDialog.Builder(context)
                        .setMessage("此操作将会删除本地文件，确定吗？")
                        .setNegativeButton(
                            "取消"
                        ) { p0, p1 -> }
                        .setPositiveButton(
                            "确定"
                        ) { p0, p1 ->
                            val success = FileUtils.delete(File(item["fileUrl"].toString()))
                            if (success) {
                                getMainData()
                            }
                        }
                        .show()
//                    XPopup.Builder(this@ServerConnectActivity)
//                        .dismissOnBackPressed(true)
//                        .dismissOnTouchOutside(true)
//                        .isDestroyOnDismiss(true)
//                        .asConfirm(
//                            "提示", "此操作将会删除本地文件，确定吗？",
//                            "取消", "确定",
//                            OnConfirmListener {
//
//                            }, null, false
//                        )
//                        .show()
                    true
                }
                setOnFocusChangeListener { view, b ->
                    if (b) {
                        view.setBackgroundColor(Color.parseColor("#4D7ec059"))
                    } else {
                        view.setBackgroundColor(Color.parseColor("#ffffff"))
                    }
                }
            }

            //获取链接
            GlideHelper.loadRoundImage(
                this@ServerConnectActivity,
                helper?.getView<ImageView>(R.id.iv_icon)!!,
                item["icon"] as Drawable
            )
            helper?.setText(R.id.tv_title, item["name"].toString())
            val tips = (if (item["isInstall"].toString()
                    .toBoolean()
            ) "\n本机已安装此包名的安装包，建议先卸载后安装" else "")
            val span =
                SpanUtils().append("应用包名：" + item["packageName"] + "\n文件名：" + item["fileName"].toString())
                    .append(tips)
                    .setForegroundColor(ResUtils.getColor(R.color.red_ef0000))
                    .create()
            helper?.setText(R.id.tv_desc, span)
            helper?.setText(R.id.tv_time, item["time"].toString() + " 更新      " + item["size"])

            helper?.getView<TextView>(R.id.tv_btn)?.apply {
                setBackgroundResource(R.drawable.selecter_bg_round_btn)
                visibility = View.VISIBLE
            }

            helper?.getView<TextView>(R.id.tv_flag)?.apply {
                text = if (item["version"].toString()
                        .contains("v")
                ) item["version"].toString() else "V" + item["version"].toString()
                //Android
                setCompoundDrawablesWithIntrinsicBounds(
                    ResUtils.getDrawable(R.mipmap.android_icon),
                    null,
                    null,
                    null
                )
            }

            helper?.getView<TextView>(R.id.tv_delete)?.apply {
                visibility = View.VISIBLE
            }
        }
    }

    fun initData() {
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