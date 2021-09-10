package com.roll.codemao.app_server;


import com.roll.codemao.app_server.org.nanohttpd.protocols.http.NanoHTTPD;
import com.roll.codemao.app_server.org.nanohttpd.protocols.http.tempfiles.ITempFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/** 
* @author cs
* @version 2018年1月19日 上午11:14:04
*/
public class UploadFile implements ITempFile {

    private final File file;

    private final OutputStream fstream;

    public UploadFile(File dir, String filename) throws IOException {
        this.file = new File(dir, filename);
        this.fstream = new FileOutputStream(this.file);
    }

    @Override
    public void delete() throws Exception {
        NanoHTTPD.safeClose(this.fstream);
        if (!this.file.delete()) {
            throw new Exception("could not delete temporary file: " + this.file.getAbsolutePath());
        }
    }

    @Override
    public String getName() {
        return this.file.getAbsolutePath();
    }

    @Override
    public OutputStream open() throws Exception {
        return this.fstream;
    }
}

