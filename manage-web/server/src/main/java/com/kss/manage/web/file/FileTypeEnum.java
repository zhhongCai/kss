package com.kss.manage.web.file;

public enum FileTypeEnum {
    ZIP(1,".zip"),
    RAR(0,".rar");
    Integer code;
    String suffix;
    FileTypeEnum(Integer code,String suffix){
        this.code = code;
        this.suffix = suffix;
    }

    public Integer getCode() {
        return code;
    }

//    public void setCode(Integer code) {
//        this.code = code;
//    }

    public String getSuffix() {
        return suffix;
    }

//    public void setSuffix(String suffix) {
//        this.suffix = suffix;
//    }
}
