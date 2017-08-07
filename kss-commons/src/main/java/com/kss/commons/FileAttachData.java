package com.kss.commons;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class FileAttachData implements Serializable{
    private static final long serialVersionUID = 2557045924726588716L;
    @ApiModelProperty(value = "文件系统DFS对应的Key",required = true)
    private String fileKey;
    @ApiModelProperty("文件名字")
    private String fileName;
    @ApiModelProperty("文件后缀")
    private String fileSuffix;

    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSuffix() {
        return fileSuffix;
    }

    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }
}
