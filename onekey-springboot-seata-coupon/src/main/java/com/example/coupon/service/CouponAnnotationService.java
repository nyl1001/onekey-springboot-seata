package com.example.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.coupon.entry.CouponEntity;

public interface CouponAnnotationService  extends IService<CouponEntity> {

    /**
     *  用注解的方式 新增一条 优惠券信息
     * @param couponEntity
     */
    public void insertOne(CouponEntity couponEntity);

    /**
     * 用注解的方式 根据id 查询数据
     * @param id
     * @return
     */
    public CouponEntity selectById(Long id);

    /**
     *  根据id 更新数据
     * @param couponEntity
     */
    public CouponEntity updateCouponEntityById(CouponEntity couponEntity);

    /**
     *   根据id 删除缓存
     * @param id
     */
    public void deleteById(Long id);
}
