package com.kss.manage.web.login;

import com.kss.commons.util.BeanUtils;
import com.kss.manage.application.bean.login.RequestLoginDto;
import com.kss.manage.application.bean.login.ResponseLoginDto;
import com.kss.manage.application.bean.login.TokenModel;
import com.kss.manage.application.bean.login.TokenResponse;
import com.kss.manage.application.service.login.LoginService;
import com.kss.manage.application.service.login.TokenManager;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@RequestMapping("/login")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    LoginService loginService;

    @Autowired
    TokenManager tokenManager;


    @PostMapping("/login")
    @ApiOperation(value="登录", notes="登录")
    public TokenResponse login(@ApiParam @RequestBody RequestLoginDto requestLoginDto,HttpServletRequest request)throws Exception{
        ResponseLoginDto responseLoginDto =
                loginService.login(requestLoginDto.getLoginId(),
                        requestLoginDto.getPassword(), request);
        if(null==responseLoginDto){
            throw new NotFoundException("没有找到对应的用户，请检查登录的用户名和密码。");
        }
        TokenResponse tokenResponse = BeanUtils.converObject(responseLoginDto,TokenResponse.class);
        return tokenResponse;
    }


    @PostMapping("/logout")
    @ApiOperation(value="注销", notes="注销")
    public void logout(@ApiParam @RequestBody TokenModel tokenModel) throws Exception{
        logger.info("LoginController:logout："+ tokenModel);
        tokenManager.deleteToken(tokenModel.getToken());
    }

}