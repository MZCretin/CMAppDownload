package com.roll.codemao.app_server;

import android.content.Context;
import android.text.TextUtils;


import com.roll.codemao.app_server.org.nanohttpd.protocols.http.IHTTPSession;
import com.roll.codemao.app_server.org.nanohttpd.protocols.http.response.Response;
import com.roll.codemao.app_server.org.nanohttpd.protocols.http.response.Status;

import java.io.IOException;


/** 
* @author cs
* @version 2018年1月18日 下午8:04:00
*/
public class ResourceDispatcher extends BaseContextDispatcher {
	 
	public ResourceDispatcher(Context context) {
		super(context);
	}

	@Override
	public Response handle(IHTTPSession session) {
		try {
            String fullPath = session.getUri();
            fullPath = fullPath.replace("%20", " ");
            String resourceName = fullPath;
            if (resourceName.startsWith("/")) {
                resourceName = resourceName.substring(1);
            }
            if (resourceName.indexOf("?") > 0) {
                resourceName = resourceName.substring(0, resourceName.indexOf("?"));
            }
			if (TextUtils.isEmpty(resourceName)) {
            	resourceName = PAGE_INDEX;
            }
            String mimeType = null;
            if (!TextUtils.isEmpty(getContentTypeByResourceName(resourceName))) {
            	mimeType = getContentTypeByResourceName(resourceName);
            	return Response.newChunkedResponse(Status.OK, mimeType, openAssets(resourceName));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		return null;
	}
}
