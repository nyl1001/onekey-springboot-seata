package com.example.coupon.controller;

import com.example.coupon.component.JedisComponent;
import com.example.coupon.service.CouponService;
import com.example.coupon.service.WarehouseService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/redisController")
public class redisController {
    @Resource(name = "initData")
    private DefaultRedisScript<Boolean> redisScriptInitData;
    @Resource(name = "setLua")
    private DefaultRedisScript<Boolean> redisScriptSet;
    @Resource(name = "delLua")
    private DefaultRedisScript<Boolean> redisScriptDel;

    @Resource(name = "doDeduct")
    private DefaultRedisScript<Boolean> doDeductRedisScript;

    @Resource(name = "cancelDeduct")
    private DefaultRedisScript<Boolean> cancelDeductRedisScript;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedisTemplate redisTemplate;

    @Autowired
    JedisComponent jedisComponent;

    @Autowired
    WarehouseService warehouseService;

    @Autowired
    private CouponService couponService;

    @GetMapping("/initData")
    public ResponseEntity initData() {
        Boolean execute = stringRedisTemplate.execute(redisScriptInitData, new ArrayList<>(), new ArrayList<>());
        return null;
    }

    @GetMapping("/initData2")
    public ResponseEntity initData2() {
        Object execute = redisTemplate.execute(redisScriptInitData, new ArrayList<>(), new ArrayList<>());
        return null;
    }

    @GetMapping("/initData3")
    public ResponseEntity initData3() {
        // 指定 lua 脚本，并且指定返回值类型
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>("redis.call('HSET', 'retailer-warehouse', 1, 1000)\n" +
                "redis.call('HSET', 'retailer-warehouse', 2, 1000)\n" +
                "redis.call('HSET', 'retailer-warehouse', 3, 1000)\n" +
                "return true", Long.class);
        // 参数一：redisScript，参数二：key列表，参数三：arg（可多个）
        Object result = redisTemplate.execute(redisScript, new ArrayList<>(), new ArrayList<>());
        return null;
    }

    @GetMapping("/initData4")
    public ResponseEntity initData4() {
        Jedis jedis = null;
        try {
            //获取Jedis实例
            jedis = jedisComponent.getJedis();
            String luaStr = "redis.call('HSET', 'retailer-warehouse', 1, 1000)\n" +
                    "redis.call('HSET', 'retailer-warehouse', 2, 1000)\n" +
                    "redis.call('HSET', 'retailer-warehouse', 3, 1000)\n" +
                    "return true";
            Object result = jedis.eval(luaStr, new ArrayList<>(), new ArrayList<>());
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/initData5")
    public ResponseEntity initData5() {
        Jedis jedis = null;
        try {
            //获取Jedis实例
            jedis = jedisComponent.getJedis();
            Object result = jedis.eval(redisScriptInitData.getScriptAsString(), new ArrayList<>(), new ArrayList<>());
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/lua")
    public ResponseEntity lua() {
        List<String> keys = Arrays.asList("testLua", "hello lua");
        Boolean execute = stringRedisTemplate.execute(redisScriptSet, keys, "100");
        return null;
    }

    @GetMapping("/lua-del")
    public ResponseEntity luaDel() {
        List<String> keys = Arrays.asList("testLua");
        Boolean execute = stringRedisTemplate.execute(redisScriptDel, keys, "hello lua");
        return null;
    }

    @GetMapping("/doDeduct")
    public ResponseEntity doDeduct() {
        Jedis jedis = null;
        try {
            //获取Jedis实例
            jedis = jedisComponent.getJedis();
            Object result = jedis.eval(doDeductRedisScript.getScriptAsString(), new ArrayList<>(), new ArrayList<>());
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/cancelDeduct")
    public ResponseEntity cancelDeduct() {
        Jedis jedis = null;
        try {
            //获取Jedis实例
            jedis = jedisComponent.getJedis();
            Object result = jedis.eval(cancelDeductRedisScript.getScriptAsString(), new ArrayList<>(), new ArrayList<>());
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/deduct")
    @Transactional
    @GlobalTransactional(timeoutMills = 60000 * 2)
    public String deductWareHouse(@RequestParam("commodityId") String commodityId,
                                  @RequestParam("count") Integer count) throws Exception {
        warehouseService.deductWarehouseFromRedis(commodityId, count);
        couponService.updateDbCouponName();
        throw new RuntimeException();
    }

}
