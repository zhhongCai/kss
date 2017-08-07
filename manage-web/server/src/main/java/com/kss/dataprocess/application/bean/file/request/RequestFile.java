package com.kss.dataprocess.application.bean.file.request;

import java.io.Serializable;

public class RequestFile implements Serializable {

    private static final long serialVersionUID = -7140563576489432228L;

    private String fileName;

    private String fileKey;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }
}
