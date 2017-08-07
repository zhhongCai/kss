package com.kss.dataprocess.configuration;

import com.github.tobato.fastdfs.proto.storage.DownloadCallback;
import java.io.IOException;
import java.io.InputStream;

public class DownLoadDFS implements DownloadCallback<String>{
    /**
     * 文件名称
     */
    private InputStream inputStream;

    public DownLoadDFS() {
    }

    @Override
    public String recv(InputStream ins) throws IOException {
        this.inputStream = ins;
        return null;
    }

    public InputStream getInputStream() {
        return inputStream;
    }
}
