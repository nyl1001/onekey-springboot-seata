package com.example.coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.coupon.entry.CouponEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CouponMapper extends BaseMapper<CouponEntity> {

}
