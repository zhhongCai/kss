package com.kss.dataprocess.application.bean.file.request;

import java.io.Serializable;

/**
 * Created by zhourj on 2017/6/8.
 */
public class RequestAttachAdd implements Serializable {

    private static final long serialVersionUID = 1480823137351327571L;
    private Long businessId;
    private String businessName;
    private String category;
    private String fileName;
    private String fileKey;
    private String fileSuffix;
    private String getCategory;

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

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

    public String getFileSuffix() {
        return fileSuffix;
    }

    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }

    public String getGetCategory() {
        return getCategory;
    }

    public void setGetCategory(String getCategory) {
        this.getCategory = getCategory;
    }
}
