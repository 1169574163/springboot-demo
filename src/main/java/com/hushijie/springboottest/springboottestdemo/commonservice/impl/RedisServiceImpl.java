package com.hushijie.springboottest.springboottestdemo.commonservice.impl;

import com.hushijie.springboottest.springboottestdemo.commonservice.RedisService;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * RedisServiceImpl
 * 对 RedisTemplate 以及 hash、string、list、set、zset 常用操作的简单封装
 *
 * @author wubin email: wubin@hushijie.com.cn
 * @date 2018/11/12
 */
@Service
public class RedisServiceImpl implements RedisService {
    @Resource
    private RedisTemplate redisTemplate;

    /**
     * 检测key是否存在
     *
     * @param key key
     * @return boolean
     */
    @Override
    public boolean existsKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 重命名key，如果newKey已经存在，则newKey的原值被覆盖
     *
     * @param oldKey oldKey
     * @param newKey newKey
     */
    @Override
    public void renameKey(String oldKey, String newKey) {
        redisTemplate.rename(oldKey, newKey);
    }

    /**
     * newKey不存在时重命名key
     *
     * @param oldKey oldKey
     * @param newKey newKey
     */
    @Override
    public boolean renameKeyNotExist(String oldKey, String newKey) {
        return redisTemplate.renameIfAbsent(oldKey, newKey);
    }

    /**
     * 删除key
     *
     * @param key key
     * @return boolean
     */
    @Override
    public boolean deleteKey(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 删除key列表
     *
     * @param keys key列表
     * @return long
     */
    @Override
    public long deleteKey(String... keys) {
        Set<String> keySet = Stream.of(keys).collect(Collectors.toSet());
        return redisTemplate.delete(keySet);
    }

    /**
     * 删除key集合
     *
     * @param keys key集合
     * @return long
     */
    @Override
    public long deleteKey(Collection<String> keys) {
        Set<String> keySet = new HashSet<>(keys);
        return redisTemplate.delete(keySet);
    }

    /**
     * 设置key的超时时间
     *
     * @param key      key
     * @param timeout  超时时间
     * @param timeUnit 时间单位
     * @return boolean
     */
    @Override
    public boolean expireKey(String key, long timeout, TimeUnit timeUnit) {
        return redisTemplate.expire(key, timeout, timeUnit);
    }

    /**
     * 指定key在超时日期
     *
     * @param key  key
     * @param date 超时日期
     * @return boolean
     */
    @Override
    public boolean expireKeyAt(String key, Date date) {
        return redisTemplate.expireAt(key, date);
    }

    /**
     * 查询key的超时时间
     *
     * @param key      key
     * @param timeUnit 时间单位
     * @return long
     */
    @Override
    public long getKeyExpire(String key, TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit);
    }

    /**
     * 将key设置为永久有效
     *
     * @param key key
     * @return boolean
     */
    @Override
    public boolean persistKey(String key) {
        return redisTemplate.persist(key);
    }

    /**
     * 获取对hash类型的数据操作对象
     *
     * @return ValueOperations<String, Object>
     */
    @Override
    public HashOperations<String, String, Object> hashOps() {
        return redisTemplate.opsForHash();
    }

    /**
     * 获取对redis字符串类型数据操作对象
     *
     * @return ValueOperations<String, Object>
     */
    @Override
    public ValueOperations<String, Object> valueOps() {
        return redisTemplate.opsForValue();
    }

    /**
     * 获取对链表类型的数据操作对象
     *
     * @return ListOperations<String, Object>
     */
    @Override
    public ListOperations<String, Object> listOps() {
        return redisTemplate.opsForList();
    }

    /**
     * 获取对无序集合类型的数据操作对象
     *
     * @return SetOperations<String, Object>
     */
    @Override
    public SetOperations<String, Object> setOps() {
        return redisTemplate.opsForSet();
    }

    /**
     * 获取对有序集合类型的数据操作对象
     *
     * @return ZSetOperations<String, Object>
     */
    @Override
    public ZSetOperations<String, Object> zSetOps() {
        return redisTemplate.opsForZSet();
    }

    /**
     * 加锁
     *
     * @param key   key
     * @param value value
     * @return boolean
     */
    @Override
    public boolean lock(String key, Object value) {
        boolean result = redisTemplate.opsForValue().setIfAbsent(key, value);
        if (result) {
            return result;
        }
        //
        return false;
    }

    @Override
    public void unlock(String key, Object value) {
        //
    }
}
