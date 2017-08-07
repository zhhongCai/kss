package com.kss.dataprocess.database.service.login;

import com.google.common.base.Strings;
import com.kss.commons.Constants;
import com.kss.commons.exceptions.ServiceException;
import com.kss.dataprocess.database.bo.user.UserBo;
import com.kss.dataprocess.database.po.LoginHistoryPo;
import com.kss.dataprocess.database.service.user.UserBoService;
import com.kss.dataprocess.application.bean.login.ResponseLoginDto;
import com.kss.dataprocess.application.service.login.LoginService;
import com.kss.dataprocess.application.service.login.TokenManager;
import com.kss.dataprocess.database.mapper.LoginHistoryMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class LoginServiceImpl implements LoginService {

    private final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    UserBoService userBoService;

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    LoginHistoryMapper loginHistoryMapper;

    @Autowired
    private Mapper<LoginHistoryPo> mapper;

    @Override
    public ResponseLoginDto login(String loginId, String password,
                                  HttpServletRequest request) throws Exception {
        UserBo userBo = null;
        try {
            userBo = userBoService.findUserByPo(new UserBo(loginId, password));
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new ServiceException("没有找到对应的用户，请检查登录的用户名和密码。");
        }
        if(null == userBo){
            return null;
        }
        //生成一个新token，保存用户登录状态并记录
        String ip = getIp(request);
        String token = null;

        //删除之前内存记录的未过时token
        LoginHistoryPo loginHistoryPo = loginHistoryMapper.getLastLogin(userBo.getId());

        if (loginHistoryPo != null) {
            token = loginHistoryPo.getToken();
            if (null == tokenManager.checkToken(token)) {
                token = null;
            }
        }
        if (Strings.isNullOrEmpty(token)) {
            token = tokenManager.createToken(userBo.getId(), ip);
            LoginHistoryPo loginHistoryPoNew = new LoginHistoryPo();
            loginHistoryPoNew.setLoginId(userBo.getCode());
            loginHistoryPoNew.setUserId(userBo.getId());
            loginHistoryPoNew.setUserName(userBo.getName());
            loginHistoryPoNew.setRequestIp(ip);
            loginHistoryPoNew.setToken(token);
            SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            loginHistoryPoNew.setLoginTime(timeFormat.format(new Date()));
            mapper.insertSelective(loginHistoryPoNew);
        }

        //返回带token的用户信息
        ResponseLoginDto responseLoginDto = new ResponseLoginDto();
        responseLoginDto.setId(userBo.getId());
        responseLoginDto.setName(userBo.getName());
        if (Constants.SUPER_MANAGER.equals(userBo.getCode())) {
            responseLoginDto.setUserType("超级管理员");
        } else {
            responseLoginDto.setUserType("普通用户");
        }
        responseLoginDto.setDepartment(userBo.getDepartment());
        responseLoginDto.setCode(userBo.getCode());
        responseLoginDto.setToken(token);
        responseLoginDto.setPhone(userBo.getPhone());
        return responseLoginDto;

    }

    private String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }
}
