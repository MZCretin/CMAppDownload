package com.roll.codemao.app_server;

import android.os.Environment;
import android.text.TextUtils;


import com.blankj.utilcode.util.AppUtils;
import com.roll.codemao.app_server.org.nanohttpd.protocols.http.NanoHTTPD;
import com.roll.codemao.app_server.org.nanohttpd.protocols.http.tempfiles.DefaultTempFileManager;
import com.roll.codemao.app_server.org.nanohttpd.protocols.http.tempfiles.ITempFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
* @author cs
* @version 2018年1月19日 上午11:04:36
*/
public class UploadFileManager extends DefaultTempFileManager {
    private final File dir;
    private final List<ITempFile> files;
    public static final String DIR_IN_SDCARD = "wifi_translate";
    
    public UploadFileManager() {
        this.dir = new File(Environment.getExternalStorageDirectory() + File.separator + DIR_IN_SDCARD+ File.separator + AppUtils.getAppPackageName());
        if (!dir.exists()) {
        	dir.mkdirs();
        }
        this.files = new ArrayList<ITempFile>();
    }
	@Override
	public void clear() {
		super.clear();
		this.files.clear();
	}

	@Override
	public ITempFile createTempFile(String filename_hint) throws Exception {
		if(!TextUtils.isEmpty(filename_hint)) {
			UploadFile file = new UploadFile(this.dir, filename_hint);
	        this.files.add(file);
	        return file;	
		}
		return super.createTempFile(filename_hint);
	}
}
