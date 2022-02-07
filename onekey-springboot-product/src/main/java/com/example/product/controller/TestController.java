package com.example.product.controller;

import com.example.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testController")
@Slf4j
public class TestController {


    /**
     *  project 测试 R
     * @return
     */
    @RequestMapping("/test")
    public R test(){
        log.info("onekey-springboot-product 测试 R 成功");
        return R.ok("onekey-springboot-product 测试 R 成功");
    }
}
