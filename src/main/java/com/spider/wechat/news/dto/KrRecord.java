package com.spider.wechat.news.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Description: 36氪爬虫抓取
 * @Author: jeecg-boot
 * @Date:   2019-07-05
 * @Version: V1.0
 */
@Data
public class KrRecord {
    
	/**id*/
	private String id;
	/**平台推文id*/
	private Long krId;
	/**标题*/
	private String titile;
	/**描述*/
	private String description;
	/**更新日期*/
	private Date updatedAt;
	/**原文链接*/
	private String newsUrl;
	/**推送日期*/
	private Date publishedAt;
	/**createBy*/
	private String createBy;
	/**createTime*/
	private Date createTime;
	/**updateBy*/
	private String updateBy;
	/**updateTime*/
	private Date updateTime;
	/**是否分析 0否 1是*/
	private Integer isAnalyze;
	/**新闻图片*/
	private String imageUrl;
	/**1:36kr 2:新闻联播*/
	private Integer oplatformCore;
}
