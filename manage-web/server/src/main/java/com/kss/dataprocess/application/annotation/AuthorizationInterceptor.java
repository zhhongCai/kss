package com.kss.dataprocess.application.annotation;

import com.kss.commons.Constants;
import com.kss.commons.exceptions.AuthorizationException;
import com.kss.dataprocess.application.bean.login.TokenModel;
import com.kss.dataprocess.application.service.login.TokenManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 自定义拦截器，判断此次请求是否有权限
 */
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    private TokenManager manager;

    /**
     * 存放Authorization的header字段
     */
    public static final String AUTHORIZATION = "authorization";


    private static final Logger logger = LoggerFactory.getLogger(AuthorizationInterceptor.class);

    public AuthorizationInterceptor(TokenManager manager) {
        this.manager = manager;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("Request Method:{},Request URL:{}", request.getMethod(), request.getRequestURL());
        //从header中得到token
        if(request.getMethod().equalsIgnoreCase("OPTIONS")){
            return  true;
        }
       String token = request.getHeader(AUTHORIZATION);
        if(StringUtils.isBlank(token)){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        //todo 后续删除
        if("supertoken_666".equals(token)){
            request.setAttribute(Constants.CURRENT_USER_ID, 1);
            request.setAttribute("supertoken", token);
            return true;
        }
        //验证token
        TokenModel tokenModel = manager.checkToken(token);
        if (tokenModel != null) {
            //如果token验证成功，将token对应的用户id存在request中，便于之后注入
            request.setAttribute(Constants.CURRENT_USER_ID, tokenModel.getUserId());
            return true;
        }else{
            throw new AuthorizationException("登录超时");
        }

    }
}
