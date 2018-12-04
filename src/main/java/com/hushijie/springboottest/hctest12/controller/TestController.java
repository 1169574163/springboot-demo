package com.hushijie.springboottest.hctest12.controller;


import com.alibaba.fastjson.JSON;
import com.hushijie.springboottest.hctest12.innerservice.TestInnerService;
import com.hushijie.springboottest.hctest12.model.TestDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 测试表 前端控制器
 * </p>
 *
 * @author wubin
 * @since 2018-12-03
 */
@RestController
@RequestMapping("/hctest12/test")
public class TestController {

    @Autowired
    private TestInnerService testInnerService;

    @GetMapping("/{id}")
    public TestDO get(@PathVariable Long id) {
        return testInnerService.getById(id);
    }

    @PostMapping("/add")
    public Boolean add(TestDO testDO) {
        System.out.println("testDO = " + testDO);
        return testInnerService.save(testDO);
    }

}

