package com.roll.codemao.app_server;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.blankj.utilcode.util.NetworkUtils;
import com.facebook.stetho.inspector.protocol.module.Network;
import com.roll.codemao.app_server.org.nanohttpd.protocols.http.IHTTPSession;
import com.roll.codemao.app_server.org.nanohttpd.protocols.http.NanoHTTPD;
import com.roll.codemao.app_server.org.nanohttpd.protocols.http.response.Response;
import com.roll.codemao.app_server.org.nanohttpd.protocols.http.response.Status;
import com.roll.codemao.base.ui.BaseApplication;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.WIFI_SERVICE;


/**
 * @author cs
 * @version 2018年1月18日 下午8:04:00
 */
public class DeviceInfoDispatcher extends BaseContextDispatcher {

	public DeviceInfoDispatcher(Context context) {
		super(context);
	}

	@Override
	public Response handle(IHTTPSession session) {
		return Response.newFixedLengthResponse(Status.OK, "application/json", getDeviceInfo(getWiFiName()));
	}
	
	private String getWiFiName() {
		return NetworkUtils.getIPAddress(true);
	}

	private String getDeviceInfo(String networkName) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("device_notify", "(WIFI安装Apk，是针对平常需要将电脑上的Apk文件安装到手机上的场景开发的工具，</br>只需要在电脑上上传apk文件，就可以在手机上进行安装，非常方便）</br>使用过程中有什么问题请联系 muxiannian@codemao.cn");
			jsonObject.put("device_desc", "当前IP：" + networkName + "</br>（传输过程中，请不要关闭点猫App助手上的WiFi传输窗口。）");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject.toString();
	}
}
