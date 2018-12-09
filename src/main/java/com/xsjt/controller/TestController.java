package com.xsjt.controller;

import com.xsjt.service.TestService;
import com.xsjt.util.ResponseBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: 喜🐑
 * @create: 2018-12-09 15:12
 */
@Api(description = "测试")
@RestController
@RequestMapping("/api")
public class TestController {
@Autowired
private TestService testService;
    @ApiOperation("得到")
    @GetMapping("/all")
    public ResponseBean getAll(){
        Map<String,Object> map=new HashMap<>();
        map.put("info",testService.listInfo());
        map.put("user",testService.listUser());
        map.put("role",testService.listRole());
        return ResponseBean.success(map);
    }

    @ApiOperation("得到")
    @GetMapping("/one")
    public ResponseBean getOne(){
        return ResponseBean.success(testService.listRole());
    }
}
