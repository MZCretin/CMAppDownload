package com.roll.codemao.app_server

import android.content.Context
import android.os.Environment
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.FileUtils
import com.roll.codemao.app_server.org.nanohttpd.protocols.http.IHTTPSession
import com.roll.codemao.app_server.org.nanohttpd.protocols.http.response.Response
import com.roll.codemao.app_server.org.nanohttpd.protocols.http.response.Status
import org.joda.time.DateTime
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File

/**
 * @author cs
 * @version 2018年1月18日 下午8:04:00
 */
class AppListDispatcher(context: Context?) : BaseContextDispatcher(context) {
    override fun handle(session: IHTTPSession): Response {
        return Response.newFixedLengthResponse(Status.OK, "application/json", appList)
    }

    //读取一下文件夹下面的文件列表
    private val appList: String
        private get() {
            //读取一下文件夹下面的文件列表
            val file = File(Environment.getExternalStorageDirectory().toString() + File.separator + UploadFileDispatcher.DIR_IN_SDCARD + File.separator + AppUtils.getAppPackageName())
            val jsonArray = JSONArray()
            if (file.exists()) {
                try {
                    val files = FileUtils.listFilesInDir(file)
                    files.sortBy { it.lastModified() }
                    for (file1 in files) {
                        val jsonObject = JSONObject()
                        jsonObject.put("name", file1.name)
                        jsonObject.put("size", ConvertUtils.byte2FitMemorySize(file1.length(), 2))
                        jsonObject.put("time", DateTime(file1.lastModified()).toString("yyyy-MM-dd HH:mm:ss"))
                        jsonArray.put(jsonObject)
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
            return jsonArray.toString()
        }
}