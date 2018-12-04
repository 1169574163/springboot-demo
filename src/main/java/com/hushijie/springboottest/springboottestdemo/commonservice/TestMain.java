package com.hushijie.springboottest.springboottestdemo.commonservice;

import org.springframework.beans.factory.BeanFactoryUtils;

import javax.annotation.Resource;

/**
 * TestMain
 *
 * @author wubin email: wubin@hushijie.com.cn
 * @date 2018/11/12
 */
public class TestMain {
    @Resource
    private RedisService redisService;
    public static void main(String[] args) {
        //
    }
}
