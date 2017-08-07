package com.kss.dataprocess.web;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class HelloController {
    public HelloController() {
    }

    @ResponseBody
    @RequestMapping(value = "/getcount", method = RequestMethod.GET)
    @ApiOperation(value="测试-getCount", notes="getCount更多说明")
    public ModelMap getCount(HttpServletRequest request,
                             HttpServletResponse response) {
        ModelMap map = new ModelMap();
        map.addAttribute("count", 158);
        map.addAttribute("xstest", "测试");
        return map;
    }
    @ResponseBody
    @RequestMapping(value = "/show", method=RequestMethod.GET)
    @ApiOperation(value="测试接口", notes="测试接口详细描述")
    public String show(
            @ApiParam(required=true, name="name", value="姓名")
            @RequestParam(name = "name", required=true) String stuName){
        return "success";
    }
    @RequestMapping(value = "/detail", method=RequestMethod.POST)
    @ApiOperation(value="获取详情", notes="测试接口详细描述")
    public String detail(@RequestParam("id") Long id){
        System.out.print(id);
        return "name="+id;
    }
}