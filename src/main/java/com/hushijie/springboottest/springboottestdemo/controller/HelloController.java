package com.hushijie.springboottest.springboottestdemo.controller;

import com.hushijie.springboottest.common.util.redislock.SimpleRedisLock;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * TestController
 *
 * @author wubin email: wubin@hushijie.com.cn
 * @date 2018/10/24
 */
@RestController
@RequestMapping("/test")
public class HelloController {

    @Resource
    private RedisTemplate redisTemplate;

    @RequestMapping("/hello")
    public String helloWorld() {
        return "hello world!";
    }

    @PostMapping("/redis1")
    public void lockRedis1(String key) {
        SimpleRedisLock<Long> lock = new SimpleRedisLock<>(redisTemplate);
        lock.execute(key, 60000L, () -> {
            System.out.println("worker1 is executed. key = " + key);
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 0L;
        });
    }

    @PostMapping("/redis2")
    public void lockRedis2(String key) {
        SimpleRedisLock<Object> lock = new SimpleRedisLock<>(redisTemplate);
        lock.execute(key, 60000L, () -> {
            System.out.println("worker2 is executed. key = " + key);
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        });
    }
}
