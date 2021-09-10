package com.roll.codemao.app_server;


import com.roll.codemao.app_server.org.nanohttpd.protocols.http.tempfiles.ITempFileManager;
import com.roll.codemao.app_server.org.nanohttpd.util.IFactory;

/**
* @author cs
* @version 2018年1月19日 上午11:03:54
*/
public class UploadFileManagerFactory implements IFactory<ITempFileManager> {

	@Override
	public ITempFileManager create() {
		 return new UploadFileManager();
	}
}