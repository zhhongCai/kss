package com.kss.core.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class KssAbstractController {

    /**
     * 查询数据
     * @param request
     * @param response
     * @return
     */
    public abstract String queryData(HttpServletRequest request, HttpServletResponse response);

    /**
     * 执行对应task
     * @param request
     * @param response
     * @return
     */
    public abstract String doWorkNow(HttpServletRequest request, HttpServletResponse response);
}
