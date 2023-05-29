package com.spider.wechat.news.controller;

import com.spider.wechat.news.service.krRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Title: IndexController.java
 * @Package com.fcbox.mobile.post.server.controller
 * @Description: (用一句话描述该文件做什么)
 * @Author: 002954
 * @Date: 2023/2/7 14:52
 * @Version V1.0
 * @Copyright: 2023 Shenzhen Hive Box Technology Co.,Ltd All rights reserved.
 * @Note: This content is limited to the internal circulation of Hive Box, and it is prohibited to leak or used for other commercial purposes.
 */
@Slf4j
@RestController
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private krRecordService krRecordService;
    /**
     * 测试页
     */
    @RequestMapping(value = "/test")
    public String test() {
        krRecordService.execute();
        return "success";
    }


}
