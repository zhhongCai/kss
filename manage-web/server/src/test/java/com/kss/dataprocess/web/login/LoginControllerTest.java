package com.kss.dataprocess.web.login;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kss.manage.ApplicationInitializer;
import com.kss.manage.application.bean.login.RequestLoginDto;
import org.apache.log4j.Logger;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author zhourj on 2017/5/25.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationInitializer.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles({"test"})
//@Ignore
public class LoginControllerTest{
    String prefix = "/login/";
    static Logger logger = Logger.getLogger(LoginControllerTest.class);
    @Autowired
    WebApplicationContext wac;
    @Autowired
    public MockMvc mvc;

    @Test
    public void test() throws Exception {
        /**
         * 登录测试
         */
        RequestLoginDto requestLoginDto = new RequestLoginDto();
        requestLoginDto.setLoginId("admin");
        requestLoginDto.setPassword("Aa123456");
        MvcResult addResult = mvc.perform(MockMvcRequestBuilders.post(prefix + "login")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("X-Forwarded-For","127.0.0.1").header("X-Real-IP","127.0.0.1")
                .content(JSONObject.toJSON(requestLoginDto).toString()))
                .andExpect(status().isOk()).andReturn();
        JSONObject resultJson = JSON.parseObject(addResult.getResponse().getContentAsString());
        String token = resultJson.getString("token");
        Assertions.assertThat(resultJson).containsKeys("token");
    }

//    @Test
//    public void testLogin() throws Exception {
//        /**
//         * 登录测试
//         */
//        RequestLoginDto requestLoginDto = new RequestLoginDto();
//        requestLoginDto.setLoginId("login1");
//        requestLoginDto.setPassword("Aa123456");
//        MvcResult addResult = mvc.perform(MockMvcRequestBuilders.post(prefix + "login")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .header("X-Forwarded-For","127.0.0.1").header("X-Real-IP","127.0.0.1")
//                .content(JSONObject.toJSON(requestLoginDto).toString()))
//                .andExpect(status().isOk()).andReturn();
//        JSONObject resultJson = JSON.parseObject(addResult.getResponse().getContentAsString());
//        ResponseMsg<Object> msg = resultJson.toJavaObject(ResponseMsg.class);
//        JSONObject object = (JSONObject) msg.getData();
//        Assertions.assertThat(object).containsKey("token");
//
//        String token = object.getString("token");
//        /**
//         * 删除测试
//         */
//        TokenModel tokenModel = new TokenModel();
//        tokenModel.setToken(token);
//        MvcResult outResult = mvc.perform(MockMvcRequestBuilders.post(prefix + "logout")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .header("X-Forwarded-For","127.0.0.1").header("X-Real-IP","127.0.0.1").header("authorization",token)
//                .content(JSONObject.toJSON(tokenModel).toString()))
//                .andExpect(status().isOk()).andReturn();
//        JSONObject outResultJson = JSON.parseObject(outResult.getResponse().getContentAsString());
//        ResponseMsg<Object> outMsg = outResultJson.toJavaObject(ResponseMsg.class);
//        JSONObject outObject = (JSONObject) outMsg.getData();
//        Assertions.assertThat(outObject).containsKey("token");
//    }
//
//    @Test
//    public void testLogout() throws Exception {
//
//    }

    @Before
    public void setUp() throws Exception {
        this.mvc = webAppContextSetup(wac)
                .alwaysDo(print())
                .build();
    }
}
