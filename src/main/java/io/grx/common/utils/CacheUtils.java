package io.grx.common.utils;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import net.jodah.expiringmap.ExpiringMap;

@Service
@Configuration
public class CacheUtils {
    private Logger logger = LoggerFactory.getLogger(getClass());
    //是否开启redis缓存  true开启   false关闭
    @Value("${spring.redis.open: #{false}}")
    private boolean redisOpen;

    @Autowired
    private RedisUtils redisUtils;

    private static ExpiringMap<String, String> map = ExpiringMap.builder()
            .variableExpiration()
            .build();

    private static ExpiringMap<String, Object> objectMap = ExpiringMap.builder()
            .variableExpiration()
            .build();

    public void put(String key, String value, long expireInSeconds) {
        if (redisOpen) {
            redisUtils.set(key, value, expireInSeconds);
        } else {
            if (expireInSeconds > 0) {
                map.put(key, value, ExpiringMap.ExpirationPolicy.CREATED, expireInSeconds, TimeUnit.SECONDS);
            } else {
                map.put(key, value);
            }
        }
    }

    public String get(String key) {
        if (redisOpen) {
            return redisUtils.get(key, String.class);
        } else {
            return map.get(key);
        }
    }

    public void delete(String key) {
        if (redisOpen) {
            redisUtils.delete(key);
        } else {
            map.remove(key);
            objectMap.remove(key);
        }
    }

    public void putObject(String key, Object value, long expireInSeconds) {
        if (redisOpen) {
            redisUtils.set(key, value, expireInSeconds);
        } else {
            if (expireInSeconds > 0) {
                objectMap.put(key, value, ExpiringMap.ExpirationPolicy.CREATED, expireInSeconds, TimeUnit.SECONDS);
            } else {
                objectMap.put(key, value);
            }
        }
    }

    public <T> T getObject(String key, Class<T> clazz) {
        if (redisOpen) {
            return redisUtils.get(key, clazz);
        } else {
            return (T) objectMap.get(key);
        }
    }
}
