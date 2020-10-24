package com.wojiushiwo.spring.security.demo.smscode.dto;

import lombok.Getter;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * @author myk
 * @create 2020/10/24 上午10:04
 */
public class SmsCode implements Serializable {

    /**
     * 验证码
     */
    @Getter
    private String code;
    /**
     * 验证码过期时间
     */
    private LocalTime expireTime;

    /**
     * 发送手机号
     */
    @Getter
    private String mobile;

    public SmsCode(String code, int expireSeconds, String mobile) {
        this.code = code;
        this.expireTime = LocalTime.now().plusSeconds(expireSeconds);
        this.mobile = mobile;
    }

    public boolean isExpired() {
        return LocalTime.now().isAfter(this.expireTime);
    }

}
