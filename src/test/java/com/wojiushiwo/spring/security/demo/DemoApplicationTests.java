package com.wojiushiwo.spring.security.demo;

import com.wojiushiwo.spring.security.demo.mapper.MyUserDetailsMapper;
import com.wojiushiwo.spring.security.demo.model.MyUserDetails;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.security.RunAs;

@SpringBootTest
class DemoApplicationTests {

    @Autowired
    private MyUserDetailsMapper myUserDetailsMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Test
    void contextLoads() {

//        MyUserDetails admin = myUserDetailsMapper.findByUserName("admin");
//        System.out.println(admin);

        String encode = passwordEncoder.encode("123456");
        System.out.println(encode);

        System.out.println(passwordEncoder.matches("123456", "$2a$10$UU17HUrihkAKXmT..vqMPO4/bfm7OJHUO/6BL/XypZ3PMr8aZr17q"));
    }

}
