package io.grx.modules.sys.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.grx.common.utils.RedisKeys;
import io.grx.common.utils.RedisUtils;
import io.grx.modules.sys.entity.SysConfigEntity;

/**
 * 系统配置Redis
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017/7/18 21:08
 */
@Component
public class SysConfigRedis {
    @Autowired
    private RedisUtils redisUtils;

    @Value("${spring.profiles.active}")
    private String profileName;

    public void saveOrUpdate(SysConfigEntity config) {
        if(config == null){
            return ;
        }
        String key = RedisKeys.getSysConfigKey(
                buildCacheKey(config.getMerchantNo(), config.getKey()));
        redisUtils.set(key, config);
    }

    public void delete(String merchantNo, String configKey) {
        String key = RedisKeys.getSysConfigKey(buildCacheKey(merchantNo, configKey));
        redisUtils.delete(key);
    }

    public SysConfigEntity get(String merchantNo, String configKey){
        String key = RedisKeys.getSysConfigKey(buildCacheKey(merchantNo, configKey));
        return redisUtils.get(key, SysConfigEntity.class);
    }

    private String buildCacheKey(String merchantNo, String key) {
        return profileName + "_" + merchantNo + "_" + key;
    }
}
