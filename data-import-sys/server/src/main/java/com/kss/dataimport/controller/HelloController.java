package com.kss.dataimport.controller;

import com.alibaba.fastjson.JSON;
import com.kss.core.controller.KssAbstractController;
import com.kss.manage.client.DbDataSourceClient;
import com.kss.manage.model.DbDataSource;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.PagedResources;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class HelloController extends KssAbstractController {

    @Autowired
    DbDataSourceClient dbDataSourceClient;

    @ResponseBody
    @RequestMapping(value = "/queryData", method = RequestMethod.GET)
    @ApiOperation(value="查询数据", notes="查询数据")
    @Override
    public String queryData(HttpServletRequest request, HttpServletResponse response) {
        PagedResources<DbDataSource> page = dbDataSourceClient.findAll();
        System.out.print(JSON.toJSONString(page));
        return JSON.toJSONString(page);
    }

    @ResponseBody
    @RequestMapping(value = "/doWorkNow", method = RequestMethod.GET)
    @ApiOperation(value="执行任务", notes="执行任务")
    @Override
    public String doWorkNow(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }
}
