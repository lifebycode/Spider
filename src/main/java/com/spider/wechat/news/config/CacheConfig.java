package com.spider.wechat.news.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Scheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;

/**
 * @Title: CacheConfig.java
 * @Package com.fcbox.blues.FcboxDemo.chain
 * @Description: 本地缓存管理器
 * @Author: 002954
 * @Date: 2023/5/9 10:00
 * @Version V1.0
 * @Copyright: 2023 Shenzhen Hive Box Technology Co.,Ltd All rights reserved.
 * @Note: This content is limited to the internal circulation of Hive Box, and it is prohibited to leak or used for other commercial purposes.
 */
@Slf4j
@Configuration
public class CacheConfig {
    /**
     * 本地缓存去重校验
     * @return
     */
    @Bean
    public Cache<Long, Boolean> caffeineCache() {
        log.info("去重线程池淘汰任务创建成功");
        return Caffeine.newBuilder()
                //设置调度器
                .scheduler(Scheduler.systemScheduler())
                //设置线程池 用于处理异步操作，如主动清理过期数据
                .executor(Executors.newFixedThreadPool(10))
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(2000)
                .build();
    }
}
