package com.kss.manage.web;

import com.kss.core.controller.KssAbstractController;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class HelloController extends KssAbstractController {
    private static Logger logger = LoggerFactory.getLogger(HelloController.class);
    public HelloController() {
    }

    @ResponseBody
    @RequestMapping(value = "/queryData", method = RequestMethod.GET)
    @ApiOperation(value="查询数据", notes="查询数据")
    @Override
    public String queryData(HttpServletRequest request, HttpServletResponse response) {
        logger.info("查询数据");
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "/doWorkNow", method = RequestMethod.GET)
    @ApiOperation(value="执行任务", notes="执行任务")
    @Override
    public String doWorkNow(HttpServletRequest request, HttpServletResponse response) {
        logger.info("执行任务");
        return null;
    }

}