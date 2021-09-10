package com.roll.codemao.app_server;

import android.os.RecoverySystem;

import com.roll.codemao.app_server.org.nanohttpd.protocols.http.IHTTPSession;
import com.roll.codemao.app_server.org.nanohttpd.protocols.http.NanoHTTPD;
import com.roll.codemao.app_server.org.nanohttpd.protocols.http.progress.ProgressListener;
import com.roll.codemao.app_server.org.nanohttpd.protocols.http.response.Response;
import com.roll.codemao.app_server.org.nanohttpd.protocols.http.response.Status;

import org.json.JSONException;
import org.json.JSONObject;


/** 
* @author cs
* @version 2018年1月24日 下午6:23:21
*/
public class UploadFileProgressDispathcer implements IDispatcher {
	private ProgressListener mListener;
	public UploadFileProgressDispathcer(ProgressListener progressListener) {
		mListener = progressListener;
	}

	@Override
	public Response handle(IHTTPSession session) {
		if (mListener != null) {
			JSONObject res = new JSONObject();
            try {
                res.put("size", mListener.getBytesRead());
                res.put("total", mListener.getContentLength());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return Response.newFixedLengthResponse(Status.OK, "application/json", res.toString());
		}
		return Response.newFixedLengthResponse("ok");
	}
}
