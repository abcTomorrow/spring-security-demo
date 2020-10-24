package com.wojiushiwo.spring.security.demo.controller;

import com.wojiushiwo.spring.security.demo.constants.MyConstants;
import com.wojiushiwo.spring.security.demo.mapper.MyUserDetailsMapper;
import com.wojiushiwo.spring.security.demo.model.MyUserDetails;
import com.wojiushiwo.spring.security.demo.smscode.dto.SmsCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Objects;

/**
 * @author myk
 * @create 2020/10/24 上午10:08
 */
@RestController
@RequestMapping
@Slf4j
public class SmsCodeController {

    @Autowired
    private MyUserDetailsMapper myUserDetailsMapper;

    @GetMapping("/smscode")
    public void sendSmsCode(@RequestParam String mobile, HttpSession session) {

        Assert.notNull(mobile, "手机号不能为空");
        MyUserDetails user = myUserDetailsMapper.findByUserMobile(mobile);
        if (Objects.isNull(user)) {
            throw new IllegalArgumentException("当前用户不存在");
        }

        String code = RandomStringUtils.randomNumeric(4);

        log.info("验证码为:" + code);

        SmsCode smsCode = new SmsCode(code, 600, mobile);

        session.setAttribute(MyConstants.SMS_CODE_SESSION_KEY, smsCode);
    }

}
