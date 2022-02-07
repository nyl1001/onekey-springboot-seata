package com.example.product.feign;


import com.example.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("onekey-springboot-seata-coupon")
public interface CouponFeignService {

    @RequestMapping("/coupon/couponController/updateDbCouponName")
    R updateDbCouponName();

}
