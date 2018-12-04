package com.hushijie.springboottest.common.util.redislock;

/**
 * AbstractRedisLock
 *
 * @author wubin email: wubin@hushijie.com.cn
 * @date 2018/11/20
 */
public abstract class AbstractRedisLock<T> implements RedisLockable {
    @Override
    public boolean lock(String key) {
        return lock(key, TIMEOUT_MILLIS, RETRY_TIMES, SLEEP_MILLIS);
    }

    @Override
    public boolean lock(String key, long expire) {
        return lock(key, expire, RETRY_TIMES, SLEEP_MILLIS);
    }

    @Override
    public boolean lock(String key, int retryTimes) {
        return lock(key, TIMEOUT_MILLIS, retryTimes, SLEEP_MILLIS);
    }

    @Override
    public boolean lock(String key, long expire, int retryTimes) {
        return lock(key, expire, retryTimes, SLEEP_MILLIS);
    }

    @Override
    public boolean lock(String key, int retryTimes, long sleepMillis) {
        return lock(key, TIMEOUT_MILLIS, retryTimes, sleepMillis);
    }

    /**
     * 执行加锁的任务
     *
     * @param key    key
     * @param executor 要执行的任务
     * @return T
     */
    public T execute(String key, RedisLockingExecutable<T> executor) {
        return this.execute(key, TIMEOUT_MILLIS, RETRY_TIMES, SLEEP_MILLIS, executor);
    }

    /**
     * 执行加锁的任务
     *
     * @param key    key
     * @param expire 过期时长（毫秒）
     * @param executor 要执行的任务
     * @return T
     */
    public T execute(String key, long expire, RedisLockingExecutable<T> executor) {
        return this.execute(key, expire, RETRY_TIMES, SLEEP_MILLIS, executor);
    }

    /**
     * 执行加锁的任务
     *
     * @param key        key
     * @param retryTimes 重试次数
     * @param executor     要执行的任务
     * @return T
     */
    public T execute(String key, int retryTimes, RedisLockingExecutable<T> executor) {
        return this.execute(key, TIMEOUT_MILLIS, retryTimes, SLEEP_MILLIS, executor);
    }

    /**
     * 执行加锁的任务
     *
     * @param key        key
     * @param expire     过期时长（毫秒）
     * @param retryTimes 重试次数
     * @param executor     要执行的任务
     * @return T
     */
    public T execute(String key, long expire, int retryTimes, RedisLockingExecutable<T> executor) {
        return this.execute(key, expire, retryTimes, SLEEP_MILLIS, executor);
    }

    /**
     * 执行加锁的任务
     *
     * @param key         key
     * @param retryTimes  重试次数
     * @param sleepMillis 重试时的睡眠时间
     * @param executor      要执行的任务
     * @return T
     */
    public T execute(String key, int retryTimes, long sleepMillis, RedisLockingExecutable<T> executor) {
        return this.execute(key, TIMEOUT_MILLIS, retryTimes, sleepMillis, executor);
    }

    /**
     * 执行加锁的任务
     *
     * @param key         key
     * @param expire      过期时长（毫秒）
     * @param retryTimes  重试次数
     * @param sleepMillis 重试时的睡眠时间
     * @param executor      要执行的任务
     * @return T
     */
    public T execute(String key, long expire, int retryTimes, long sleepMillis, RedisLockingExecutable<T> executor) {
        T t = null;
        if (this.lock(key, expire, retryTimes, sleepMillis)) {
            try {
                t = executor.doInRedisLocking();
            } finally {
                this.releaseLock(key);
            }
        }
        return t;
    }
}
