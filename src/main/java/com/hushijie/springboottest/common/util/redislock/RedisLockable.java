package com.hushijie.springboottest.common.util.redislock;

/**
 * RedisLockable
 *
 * @author wubin email: wubin@hushijie.com.cn
 * @date 2018/11/20
 */
public interface RedisLockable {

    String LOCK_KEY_PREFIX = "redis:lock:";

    long TIMEOUT_MILLIS = 15000;

    int RETRY_TIMES = Integer.MAX_VALUE;

    long SLEEP_MILLIS = 500;

    /**
     * 加锁
     *
     * @param key key
     * @return boolean
     */
    boolean lock(String key);

    /**
     * 加锁
     *
     * @param key    key
     * @param expire 过期时长（毫秒）
     * @return boolean
     */
    boolean lock(String key, long expire);

    /**
     * 加锁
     *
     * @param key        key
     * @param retryTimes 重试次数
     * @return boolean
     */
    boolean lock(String key, int retryTimes);

    /**
     * 加锁
     *
     * @param key        key
     * @param expire     过期时长（毫秒）
     * @param retryTimes 重试次数
     * @return boolean
     */
    boolean lock(String key, long expire, int retryTimes);

    /**
     * 加锁
     *
     * @param key         key
     * @param retryTimes  重试次数
     * @param sleepMillis 重试时的睡眠时间
     * @return boolean
     */
    boolean lock(String key, int retryTimes, long sleepMillis);

    /**
     * 加锁
     *
     * @param key         key
     * @param expire      过期时长（毫秒）
     * @param retryTimes  重试次数
     * @param sleepMillis 重试时的睡眠时间
     * @return boolean
     */
    boolean lock(String key, long expire, int retryTimes, long sleepMillis);

    /**
     * 释放锁
     *
     * @param key key
     * @return boolean
     */
    boolean releaseLock(String key);
}
