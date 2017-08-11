package com.kss.manage.application.service.login;


import com.kss.manage.application.bean.login.TokenModel;

public interface TokenManager {

    /**
     * 创建一个token关联上指定用户
     * @param userId 指定用户的id
     * @param ip 当前请求ip
     * @return 生成的token
     */
    public String createToken(long userId,String ip);

    /**
     * 检查token是否有效
     * @param token token
     * @return 是否有效
     */
    public TokenModel checkToken(String token);

    /**
     * 清除token
     * @param token 登录用户的token
     */
    public void deleteToken(String token) throws Exception;

    /**
     * 获取用户id
     * @param token
     * @return
     * @throws Exception
     */
    public Long getUserId(String token) throws Exception;

}

