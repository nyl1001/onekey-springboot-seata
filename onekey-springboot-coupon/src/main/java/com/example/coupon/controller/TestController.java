package com.example.coupon.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  测试控制器
 */
@RestController
@RequestMapping("/testController")
@Slf4j
public class TestController {

    /**
     *  该服务的测试代码
     * @return
     */
    @RequestMapping("/test")
    public String test(){
        String str = "onekey-springboot-coupon 开始测试代码";
        log.info(str);
        return str;
    }
}
