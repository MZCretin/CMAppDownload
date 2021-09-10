package com.roll.codemao.app_server;

import com.roll.codemao.app_server.org.nanohttpd.protocols.http.IHTTPSession;
import com.roll.codemao.app_server.org.nanohttpd.protocols.http.response.Response;
import com.roll.codemao.app_server.org.nanohttpd.protocols.http.response.Status;

import java.util.Random;

import static com.roll.codemao.app_server.org.nanohttpd.protocols.http.NanoHTTPD.MIME_PLAINTEXT;


/** 
* @author cs
* @version 2018年1月25日 下午3:34:15
*/
public class LongPollingDispatcher implements IDispatcher {

	@Override
	public Response handle(IHTTPSession session) {
		try {
			Thread.sleep(15000 + new Random().nextInt(1000));
		} catch (Exception e) {
		}
		return Response.newFixedLengthResponse(Status.OK,MIME_PLAINTEXT, "");
	}
}