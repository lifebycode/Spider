package com.spider.wechat.news.job;

import com.spider.wechat.news.service.krRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Title: SpiderJob.java
 * @Package com.spider.wechat.news.job
 * @Description: (用一句话描述该文件做什么)
 * @Author: 002954
 * @Date: 2023/5/26 10:35
 * @Version V1.0
 * @Copyright: 2023 Shenzhen Hive Box Technology Co.,Ltd All rights reserved.
 * @Note: This content is limited to the internal circulation of Hive Box, and it is prohibited to leak or used for other commercial purposes.
 */
@Slf4j
@Component
public class SpiderJob implements InitializingBean, DisposableBean {
    //定义定时任务类
   private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

   @Autowired
   private krRecordService krRecordService;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("开始执行定时任务");

        executor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                krRecordService.execute();
            }
        },0, 60, TimeUnit.SECONDS);//每60秒执行一次
    }

    @Override
    public void destroy() throws Exception {
        if (!executor.isShutdown()) {
            executor.shutdown();
        }
        log.info("结束定时任务");
    }
}
