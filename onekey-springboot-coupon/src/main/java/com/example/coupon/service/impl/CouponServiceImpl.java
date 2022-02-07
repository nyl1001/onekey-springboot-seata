package com.example.coupon.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.coupon.utils.RedisUtils;
import com.example.coupon.entry.CouponEntity;
import com.example.coupon.mapper.CouponMapper;
import com.example.coupon.service.CouponService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 1. 缓存穿透 是指用户查询数据，在数据库没有，自然在缓存中也不会有。这样就导致用户查询的时候， 在缓存中找不到对应key的value，每次都要去数据库再查询一遍，然后返回空(相当于进行了两次 无用的查询)。这样请求就绕过缓存直接查数据库。
 * 解决方案：缓存空值
 * 2. 缓存击穿 是指缓存中没有但数据库中有的数据（一般是缓存时间到期），这时由于并发用户特别多，同时读缓存没读到数据，又同时去数据库去取数据，引起数据库压力瞬间增大，造成过大压力。
 * 解决方案：加互斥锁
 * <p>
 * 3. 缓存雪崩 假如很多的缓存同一时刻失效，此时正好大量的请求进来了，有可能会发生同一时刻都去查询数据库，因此就发生了缓存雪崩问题
 * 解决： 设置不同的过期时间
 * <p>
 * 4. redis分布式锁： 当次服务部署多个时， 结合gateway，则会出现 redis 分部署 访问问题
 * 解决： 添加分布式锁
 */
@Service
@Slf4j
public class CouponServiceImpl extends ServiceImpl<CouponMapper, CouponEntity> implements CouponService {


    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ValueOperations<String, String> valueOperations;

    @Autowired
    private HashOperations<String, String, String> hashOperations;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 新增一条 优惠券信息
     *
     * @param couponEntity
     */
    public void insertOne(CouponEntity couponEntity) {
        couponMapper.insert(couponEntity);
    }


    /**
     * 根据id 查询数据
     * 缓存雪崩
     * 解决 设置不同的过期时间
     * ValueOperations<String, String>
     *
     * @param id
     * @return
     */
    public CouponEntity selectById05(Long id) {
        synchronized (this) {
            String key = "#couponEntity." + id.toString();
            CouponEntity couponEntity = new CouponEntity();

            String valueReids = valueOperations.get(key);
            if (StringUtils.isEmpty(valueReids)) {
                valueReids = valueOperations.get(key);
                if (StringUtils.isEmpty(valueReids)) {
                    couponEntity = couponMapper.selectById(id);
                    log.info("我查询了数据库");
                    // 取 10 -20 中的随机数
                    int number = (int) (10 + Math.random() * (20 - 10 + 1));
                    long time = Long.parseLong(number + "");
                    if (couponEntity == null) { // 如果couponEntity是 null, 则存入缓存10分钟，防止此id，一直查询数据库
                        String value = redisUtils.objectParseString(couponEntity);
                        valueOperations.set(key, value, time, TimeUnit.MINUTES);
                    } else {
                        String value = redisUtils.objectParseString(couponEntity);
                        valueOperations.set(key, value, time, TimeUnit.MINUTES);
                    }
                } else {
                    log.info("获取锁之后，我查询了redis缓存");
                    couponEntity = (CouponEntity) redisUtils.stringParseObject(valueReids, couponEntity);
                }
            } else {
                log.info("我查询了redis缓存");
                couponEntity = (CouponEntity) redisUtils.stringParseObject(valueReids, couponEntity);
            }
            return couponEntity;
        }
    }


    /**
     * 根据id 查询数据
     * redis分布式锁
     * 解决： 添加分布式锁
     *
     * @param id
     * @return
     */
    public CouponEntity selectById(Long id) {
        CouponEntity couponEntity = new CouponEntity();
        String key = "lock";
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        // 1. 占分布式锁，去redis中占坑  置过期时间和占位必须是原子的
        Boolean flag = valueOperations.setIfAbsent(key, uuid, 30, TimeUnit.SECONDS);
        if (flag) {  // 加锁成功
            try {   // 加锁成功，执行业务
                couponEntity = selectById05(id);
            } finally {
                String value = valueOperations.get(key);
                if (uuid.equals(value)) {
                    redisTemplate.delete(key); // 删除锁
                }

//                String script = "if redis.call('get', KEYS[1])== ARGV[1] then returnredis.call('del', KEYS[1])else return 0 end";
//                //删除锁
//                Long lock1 = redisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList("lock"), uuid);
            }
        } else { // 加锁失败，重试， synchronized
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            couponEntity = selectById(id);
        }
        return couponEntity;
    }


    /**
     * 批量新增数据
     */
    public void insertBatchCoupon() {
        Collection<CouponEntity> entityList = new ArrayList<CouponEntity>();

        for (int i = 0; i < 100; i++) {
            CouponEntity couponEntity = new CouponEntity();
            String couponName = "测试分页功能" + i;
            couponEntity.setCouponName(couponName);
            entityList.add(couponEntity);
        }
        boolean b = this.saveBatch(entityList);
        log.info("批量新增数据：" + b);
    }

    /**
     * 从 商品哪儿调用  用来测试 seata
     */
    @Transactional
    public void updateDbCouponName() {
        CouponEntity couponEntity = new CouponEntity();
        couponEntity.setId(1L);
        couponEntity.setCouponName("从 商品哪儿调用  用来测试 seata02");
        couponMapper.updateById(couponEntity);
//        int number = 2/0;
    }

}
