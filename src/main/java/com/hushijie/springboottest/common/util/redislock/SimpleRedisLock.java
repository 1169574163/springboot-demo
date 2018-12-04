package com.hushijie.springboottest.common.util.redislock;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.UUID;

/**
 * SimpleRedisLock
 *
 * @author wubin email: wubin@hushijie.com.cn
 * @date 2018/11/20
 */
public class SimpleRedisLock<T> extends AbstractRedisLock<T> {

    private final Logger logger = LoggerFactory.getLogger(SimpleRedisLock.class);

    private final String LOCK_KEY_PREFIX = "simple:redis:lock:";

    private final StringRedisTemplate stringRedisTemplate;

    private ThreadLocal<String> lockFlag = new ThreadLocal<>();

    private static final long SUCCESS = 1L;

    public SimpleRedisLock(RedisTemplate redisTemplate) {
        super();
        this.stringRedisTemplate = new StringRedisTemplate();
        if (null != redisTemplate && null != redisTemplate.getConnectionFactory()) {
            this.stringRedisTemplate.setConnectionFactory(redisTemplate.getConnectionFactory());
            this.stringRedisTemplate.afterPropertiesSet();
        }
    }

    @Override
    public boolean releaseLock(String key) {
        Assert.isTrue(!StringUtils.isEmpty(key), "key can not be null.");
        long startTime = System.currentTimeMillis();
        try {
            final String unlockLua = "if redis.call('get', KEYS[1]) == ARGV[1] "
                    + "then return redis.call('del', KEYS[1]) else return 0 end";
            RedisScript<Long> redisScript = new DefaultRedisScript<>(unlockLua, Long.class);
            Long result = stringRedisTemplate
                    .execute(redisScript, Collections.singletonList(LOCK_KEY_PREFIX + key), lockFlag.get());
            if (null != result && SUCCESS == result) {
                lockFlag.remove();
                logger.debug("release lock succeed. use time: " + (System.currentTimeMillis() - startTime));
                return true;
            }
        } catch (Exception e) {
            logger.error("release lock occured an exception", e);
            logger.debug("release lock failed. use time: " + (System.currentTimeMillis() - startTime));
        }
        return false;
    }

    @Override
    public boolean lock(String key, long expire, int retryTimes, long sleepMillis) {
        boolean result = setLock(key, expire);
        while ((!result) && retryTimes-- > 0) {
            try {
                logger.debug("lock failed, retrying...(" + retryTimes + ")");
                Thread.sleep(sleepMillis);
            } catch (InterruptedException e) {
                return false;
            }
            result = setLock(key, expire);
        }
        return result;
    }

    private boolean setLock(String key, long expire) {
        Assert.isTrue(!StringUtils.isEmpty(key), "key can not be null.");
        long startTime = System.currentTimeMillis();
        try {
            final String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            final String lockLua = "if redis.call('set', KEYS[1], ARGV[1], 'NX' ,'PX', " + expire + ") "
                    + "then return 1 else return 0 end";
            RedisScript<Long> redisScript
                    = new DefaultRedisScript<>(lockLua, Long.class);
            Long result = stringRedisTemplate
                    .execute(redisScript, Collections.singletonList(LOCK_KEY_PREFIX + key), uuid);
            if (null != result && SUCCESS == result) {
                lockFlag.set(uuid);
                logger.debug("set lock succeed. use time: " + (System.currentTimeMillis() - startTime));
                return true;
            }
        } catch (Exception e) {
            logger.error("set redis occured an exception", e);
            logger.debug("set lock failed. use time: " + (System.currentTimeMillis() - startTime));
        }
        return false;
    }
}
