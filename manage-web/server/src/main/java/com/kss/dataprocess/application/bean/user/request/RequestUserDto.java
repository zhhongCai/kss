package com.kss.dataprocess.application.bean.user.request;

import com.kss.commons.RequestPage;

public class RequestUserDto extends RequestPage {

    private static final long serialVersionUID = 6778841876218948471L;

    private String searchInfo;//查询信息（登陆id，姓名，手机号码，部门）

    public String getSearchInfo() {
        return searchInfo;
    }

    public void setSearchInfo(String searchInfo) {
        this.searchInfo = searchInfo;
    }
}
