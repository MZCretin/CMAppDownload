package com.roll.codemao.app_server;


import com.roll.codemao.app_server.org.nanohttpd.protocols.http.IHTTPSession;
import com.roll.codemao.app_server.org.nanohttpd.protocols.http.response.Response;

/**
* @author cs
* @version 2018年1月18日 下午7:58:55
*/
public interface IDispatcher {
	Response handle(IHTTPSession session);
}
