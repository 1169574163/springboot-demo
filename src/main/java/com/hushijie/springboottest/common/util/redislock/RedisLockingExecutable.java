package com.hushijie.springboottest.common.util.redislock;

/**
 * RedisLockingExecutable
 *
 * @author wubin email: wubin@hushijie.com.cn
 * @date 2018/11/27
 */
public interface RedisLockingExecutable<T> {

    /**
     * 获取 redis 锁后执行的任务
     *
     * @return T
     */
    T doInRedisLocking();
}
