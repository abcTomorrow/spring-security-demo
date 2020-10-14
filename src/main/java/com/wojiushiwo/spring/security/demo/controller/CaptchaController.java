package com.wojiushiwo.spring.security.demo.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.wojiushiwo.spring.security.demo.constants.MyConstants;
import com.wojiushiwo.spring.security.demo.model.CaptchaImageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;

/**
 * @Classname CaptchaController
 * @Description 获取验证码控制器
 * @Date 2020/10/14 10:20 下午
 * @Created by wojiushiwo
 */
@RestController
public class CaptchaController {

    @Autowired
    private DefaultKaptcha captchaProducer;

    //该url应该需要被security运行访问
    @GetMapping("/kaptcha")
    public void kaptcha(HttpSession session, HttpServletResponse response) throws Exception {

        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store,no-cache,must-revalidate");
        response.addHeader("Cache-Control", "post-check=0,pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("/image/jpeg");

        String capText = captchaProducer.createText();

        CaptchaImageVO captchaImageVO = new CaptchaImageVO(capText, 2 * 60);

        //将验证码存到session
        session.setAttribute(MyConstants.CAPATCHA_SESSION_KEY, captchaImageVO);

        //将图片返回给前端
        try (ServletOutputStream out = response.getOutputStream();) {
            BufferedImage image = captchaProducer.createImage(capText);
            ImageIO.write(image, "jpg", out);
            out.flush();
        }
    }
}
