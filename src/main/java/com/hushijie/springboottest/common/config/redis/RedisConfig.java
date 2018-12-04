package com.hushijie.springboottest.common.config.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.lang.Nullable;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import java.nio.charset.Charset;

/**
 * RedisConfig
 *
 * @author wubin email: wubin@hushijie.com.cn
 * @date 2018/11/12
 */
@Configuration
@EnableCaching
@EnableRedisHttpSession(
        maxInactiveIntervalInSeconds = 60 * 60 * 24 * 7,
        redisNamespace = "spring:http:session")
public class RedisConfig extends CachingConfigurerSupport {
    /**
     * 自定义生成key的规则
     */
    @Override
    @Bean
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            for (Object obj : params) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };
    }

    /**
     * 设置 RedisTemplate 的序列化
     * key 使用 StringRedisSerializer
     * value 使用 FastJson2RedisSerializer
     *
     * @param factory RedisConnectionFactory
     * @return RedisTemplate<String, String>
     */
    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory factory) {
        //key 使用 StringRedisSerializer
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        //value 使用 FastJsonRedisSerializer
        FastJson2RedisSerializer<Object> fastJson2RedisSerializer = new FastJson2RedisSerializer<>(Object.class);
        //设置 RedisTemplate
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(fastJson2RedisSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(fastJson2RedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * 采用RedisCacheManager作为缓存管理器
     *
     * @param redisTemplate
     * @return CacheManager
     */
    @Bean
    public CacheManager cacheManager(RedisTemplate<Object, Object> redisTemplate) {
        return RedisCacheManager.create(redisTemplate.getConnectionFactory());
    }

    /**
     * 对hash类型的数据操作
     *
     * @param redisTemplate
     * @return HashOperations<String, String, Object>
     */
    @Bean
    public <HK, HV> HashOperations<Object, HK, HV> hashOperations(RedisTemplate<Object, Object> redisTemplate) {
        return redisTemplate.opsForHash();
    }

    /**
     * 对redis字符串类型数据操作
     *
     * @param redisTemplate
     * @return ValueOperations<String, Object>
     */
    @Bean
    public ValueOperations<Object, Object> valueOperations(RedisTemplate<Object, Object> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    /**
     * 对链表类型的数据操作
     *
     * @param redisTemplate
     * @return ListOperations<String, Object>
     */
    @Bean
    public ListOperations<Object, Object> listOperations(RedisTemplate<Object, Object> redisTemplate) {
        return redisTemplate.opsForList();
    }

    /**
     * 对无序集合类型的数据操作
     *
     * @param redisTemplate
     * @return SetOperations<String, Object>
     */
    @Bean
    public SetOperations<Object, Object> setOperations(RedisTemplate<Object, Object> redisTemplate) {
        return redisTemplate.opsForSet();
    }

    /**
     * 对有序集合类型的数据操作
     *
     * @param redisTemplate
     * @return ZSetOperations<String, Object>
     */
    @Bean
    public ZSetOperations<Object, Object> zSetOperations(RedisTemplate<Object, Object> redisTemplate) {
        return redisTemplate.opsForZSet();
    }

    /**
     * FastJson2RedisSerializer
     *
     * @author wubin email: wubin@hushijie.com.cn
     * @date 2018/11/12
     * @param <T>
     */
    private class FastJson2RedisSerializer<T> implements RedisSerializer<T> {
        public final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

        private Class<T> clazz;

        public FastJson2RedisSerializer(Class<T> clazz) {
            super();
            this.clazz = clazz;
        }

        @Nullable
        @Override
        public byte[] serialize(@Nullable T t) throws SerializationException {
            return (null == t) ? new byte[0]
                    : JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
        }

        @Nullable
        @Override
        public T deserialize(@Nullable byte[] bytes) throws SerializationException {
            return (bytes == null || bytes.length <= 0) ? null
                    : JSON.parseObject(new String(bytes, DEFAULT_CHARSET), clazz);
        }
    }
}
