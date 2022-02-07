package com.example.coupon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.coupon.entry.CouponEntity;
import com.example.coupon.mapper.CouponMapper;
import com.example.coupon.service.CouponAnnotationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class CouponAnnotationServiceImpl extends ServiceImpl<CouponMapper, CouponEntity> implements CouponAnnotationService {

    @Autowired
    private CouponMapper couponMapper;


    /**
     *  用注解的方式 新增一条 优惠券信息
     * @param couponEntity
     */
    @Override
    public void insertOne(CouponEntity couponEntity) {
        couponMapper.insert(couponEntity);
    }

    /**
     * 用注解的方式 根据id 查询数据
     * @param id
     * @return
     */
    @Override
    @Cacheable(cacheNames = "selectById")
    public CouponEntity selectById(Long id) {
        log.info("缓存注解  我查询了数据库");
        CouponEntity couponEntity = couponMapper.selectById(id);
        return couponEntity;
    }

    
    /**
     *   用注解的方式 根据id 更新数据
     * @param couponEntity
     */
    @CachePut(value="selectById",key = "#couponEntity.id")
    public CouponEntity updateCouponEntityById(CouponEntity couponEntity){
        int i = couponMapper.updateById(couponEntity);
        couponEntity = couponMapper.selectById(couponEntity.getId());
        return couponEntity;
    }


    /**
     *   根据id 删除缓存
     *   @CacheEvict(value="selectById", key = "#id")  删除对应的缓存
     *   @CacheEvict(value="selectById",allEntries = true,beforeInvocation = true)
     *      allEntries = true （是否删除该缓存名中所有数据，默认为false）
     *      beforeInvocation = true（缓存清除是否在方法之前执行，默认false，代表在方法执行之后执行）
     * @param id
     */
//    @CacheEvict(value="selectById", key = "#id")
    @CacheEvict(value="selectById",allEntries = true,beforeInvocation = true)
    public void deleteById(Long id){
        int i = couponMapper.deleteById(id);
    }
}
