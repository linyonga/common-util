package com.lianjia.fn.utils.duplicate;

import com.lianjia.fn.common.shiro.RedisManager;
import com.lianjia.fn.duplicate.SimpleDistributedLock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yangxiaochen
 * @date 2016/10/27 13:06
 */
@Configuration
public class Config {
    @Bean
    public RedisManager getRedisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.init();
        return redisManager;
    }

    @Bean
    public SimpleDistributedLock simpleDistributedLock(RedisManager redisManager) {
        SimpleDistributedLock simpleDistributedLock = new SimpleDistributedLock();
        simpleDistributedLock.setRedisManager(redisManager);
        return simpleDistributedLock;
    }

}
