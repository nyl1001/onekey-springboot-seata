package com.example.coupon.controller;

import com.example.common.utils.R;
import com.example.coupon.entry.CouponEntity;
import com.example.coupon.service.CouponAnnotationService;
import com.example.coupon.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/couponController")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponAnnotationService couponAnnotationService;


    /**
     *  新增一条 优惠券信息   127.0.0.1:88/api/coupon/couponController/insertOne
     {
     "couponType":0,
     "couponName":"商场优惠券",
     "num":10,
     "amount":100,
     "perLimit":1,
     "minPoint":50.5,
     "startTime":"2021-08-09 10:10:10",
     "endTime":"2021-09-09 10:10:10",
     "useType":0,
     "publishCount":8,
     "useCount":0,
     "receiveCount":0,
     "publish":1,
     "note":"测试新建优惠券"
     }
     * @param couponEntity
     * @return
     */
    @RequestMapping("/insertOne")
    public R insertOne(@RequestBody CouponEntity couponEntity){
        couponService.insertOne(couponEntity);
        return R.ok();
    }

    /**
     *  根据id 查询数据   127.0.0.1:88/api/coupon/couponController/selectById?id=1

     * @param id
     * @return
     */
    @GetMapping("/selectById")
    public R selectById(@RequestParam Long id){
        CouponEntity couponEntity = couponService.selectById(id);
        Map<String, Object> map = new HashMap<>();
        map.put("couponEntity", couponEntity);
        return R.ok(map);
    }

    /**
     *  用注解的方式 新增一条 优惠券信息
     * @param couponEntity
     * @return
     */
    @RequestMapping("/insertOneAnnotation")
    public R insertOneAnnotation(@RequestBody CouponEntity couponEntity){
        couponAnnotationService.insertOne(couponEntity);
        return R.ok();
    }

    /**
     *  用注解的方式 根据id 查询数据  127.0.0.1:88/api/coupon/couponController/selectById?id=1
     * @param id
     * @return
     */
    @GetMapping("/selectByIdAnnotation")
    public R selectByIdAnnotation(@RequestParam Long id){
        CouponEntity couponEntity = couponAnnotationService.selectById(id);
        Map<String, Object> map = new HashMap<>();
        map.put("couponEntity", couponEntity);
        return R.ok(map);
    }

    /**
     *  根据id 更新数据
     * @param couponEntity
     * @return
     */
    @PostMapping("/updateCouponEntityById")
    public R updateCouponEntityById(@RequestBody CouponEntity couponEntity){
        CouponEntity newCouponEntity = couponAnnotationService.updateCouponEntityById(couponEntity);
        Map<String, Object> map = new HashMap<>();
        map.put("couponEntity", newCouponEntity);
        return R.ok(map);
    }

    /**
     *  根据id 删除缓存
     *      1. 删除该缓存名字下的所有缓存
     *      2. 删除对应的缓存
     * @param id
     * @return
     */
    @GetMapping("/deleteById")
    public R deleteById(@RequestParam Long id){
        couponAnnotationService.deleteById(id);
        return R.ok();
    }

    /**
     *  批量新增数据
     * @return
     */
    @RequestMapping("/insertBatchCoupon")
    public R insertBatchCoupon(){
        couponService.insertBatchCoupon();
        return R.ok();
    }

    /**
     *  从 商品哪儿调用  用来测试 seata
     * @return
     */
    @RequestMapping("/updateDbCouponName")
    public R updateDbCouponName(){
        try {
            couponService.updateDbCouponName();
            return R.ok();
        } catch (Exception ex){
            return R.error(ex.getMessage());
        }

    }

}
