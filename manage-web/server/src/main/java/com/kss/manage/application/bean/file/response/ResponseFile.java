package com.kss.manage.application.bean.file.response;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class ResponseFile implements Serializable {

    private static final long serialVersionUID = -8706262156337468406L;
    @ApiModelProperty(name = "文件名")
    private String fileName;
    @ApiModelProperty(name = "对应文件系统的key")
    private String fileKey;
    @ApiModelProperty(name = "文件系统的上下文")
    private String fileUrl;

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

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    @Override
    public String toString() {
        return "ResponseFile{" +
                "fileName='" + fileName + '\'' +
                ", fileKey='" + fileKey + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                '}';
    }
}
