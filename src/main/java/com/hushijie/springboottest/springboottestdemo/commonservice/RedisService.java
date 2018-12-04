package com.hushijie.springboottest.springboottestdemo.commonservice;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Collection;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * RedisService
 * 对 RedisTemplate 以及 hash、string、list、set、zset 常用操作的简单封装
 *
 * @author wubin email: wubin@hushijie.com.cn
 * @date 2018/11/12
 */
public interface RedisService {
    /**
     * 不设置过期时长
     */
    long NOT_EXPIRE = -1;

    /**
     * 默认过期时长，单位：秒
     */
    long DEFAULT_EXPIRE = 60 * 60 * 24;

    //////////////////////////////// 对 key 的相关操作 ////////////////////////////////

    /**
     * 检测key是否存在
     *
     * @param key key
     * @return boolean
     */
    boolean existsKey(String key);

    /**
     * 重命名key，如果newKey已经存在，则newKey的原值被覆盖
     *
     * @param oldKey oldKey
     * @param newKey newKey
     */
    void renameKey(String oldKey, String newKey);

    /**
     * newKey不存在时重命名key
     *
     * @param oldKey oldKey
     * @param newKey newKey
     */
    boolean renameKeyNotExist(String oldKey, String newKey);

    /**
     * 删除key
     *
     * @param key key
     * @return boolean
     */
    boolean deleteKey(String key);

    /**
     * 删除key列表
     *
     * @param keys key列表
     * @return long
     */
    long deleteKey(String... keys);

    /**
     * 删除key集合
     *
     * @param keys key集合
     * @return long
     */
    long deleteKey(Collection<String> keys);

    /**
     * 设置key的超时时间
     *
     * @param key      key
     * @param timeout  超时时间
     * @param timeUnit 时间单位
     * @return boolean
     */
    boolean expireKey(String key, long timeout, TimeUnit timeUnit);

    /**
     * 指定key在超时日期
     *
     * @param key  key
     * @param date 超时日期
     * @return boolean
     */
    boolean expireKeyAt(String key, Date date);

    /**
     * 查询key的超时时间
     *
     * @param key      key
     * @param timeUnit 时间单位
     * @return long
     */
    long getKeyExpire(String key, TimeUnit timeUnit);

    /**
     * 将key设置为永久有效
     *
     * @param key key
     * @return boolean
     */
    boolean persistKey(String key);

    ////////////////////////// 获取 redis 数据类型的操作对象 ///////////////////////////

    /**
     * 获取对hash类型的数据操作对象
     *
     * @return ValueOperations<String, Object>
     */
    HashOperations<String, String, Object> hashOps();

    /**
     * 获取对redis字符串类型数据操作对象
     *
     * @return ValueOperations<String, Object>
     */
    ValueOperations<String, Object> valueOps();

    /**
     * 获取对链表类型的数据操作对象
     *
     * @return ListOperations<String, Object>
     */
    ListOperations<String, Object> listOps();

    /**
     * 获取对无序集合类型的数据操作对象
     *
     * @return SetOperations<String, Object>
     */
    SetOperations<String, Object> setOps();

    /**
     * 获取对有序集合类型的数据操作对象
     *
     * @return ZSetOperations<String, Object>
     */
    ZSetOperations<String, Object> zSetOps();

    ////////////////////////////// 操作 redis 分布式锁 ////////////////////////////////

    /**
     * 加锁
     *
     * @param key   key
     * @param value value
     * @return boolean
     */
    boolean lock(String key, Object value);

    /**
     * 解锁
     *
     * @param key   key
     * @param value value
     */
    void unlock(String key, Object value);
}
