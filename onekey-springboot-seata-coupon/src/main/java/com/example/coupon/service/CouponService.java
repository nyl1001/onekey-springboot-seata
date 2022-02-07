package com.example.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.coupon.entry.CouponEntity;

public interface CouponService extends IService<CouponEntity> {

    /**
     * 新增一条 优惠券信息
     * @param couponEntity
     */
    public void insertOne(CouponEntity couponEntity);

    /**
     * 根据id 查询数据
     * @param id
     * @return
     */
    public CouponEntity selectById(Long id);

    /**
     * 批量新增数据
     */
    public void  insertBatchCoupon();

    /**
     * 从 商品哪儿调用  用来测试 seata
     */
    public void  updateDbCouponName();


}
