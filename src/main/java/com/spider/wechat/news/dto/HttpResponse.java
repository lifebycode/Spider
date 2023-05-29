package com.spider.wechat.news.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * http请求响应封装
 */
@Getter
@Setter
public class HttpResponse implements Serializable {

    /**
     * http响应状态码
     */
    private int httpStatus;

    /**
     * 请求花费的毫秒数
     */
    private long costsMilliseconds;

    /**
     * String类型的result
     */
    private String stringResult;
    public HttpResponse(){}
    public HttpResponse(int httpStatus, long costsMilliseconds, String stringResult) {
        this.httpStatus = httpStatus;
        this.costsMilliseconds = costsMilliseconds;
        this.stringResult = stringResult;
    }
}
