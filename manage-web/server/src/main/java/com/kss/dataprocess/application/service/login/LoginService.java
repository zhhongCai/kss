package com.kss.dataprocess.application.service.login;

import com.kss.dataprocess.application.bean.login.ResponseLoginDto;

import javax.servlet.http.HttpServletRequest;

public interface LoginService {

    /**
     * 登录校验，成功时返回带有token的用户对象
     * @param loginId 登录账号
     * @param password 密码
     * @param request  请求
     * @return
     * @throws Exception
     */
    public ResponseLoginDto login(String loginId, String password, HttpServletRequest request) throws Exception;

}
