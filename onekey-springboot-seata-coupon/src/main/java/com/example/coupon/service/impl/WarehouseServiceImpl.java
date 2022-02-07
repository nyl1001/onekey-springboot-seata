package com.example.coupon.service.impl;


import com.example.coupon.component.JedisComponent;
import com.example.coupon.service.WarehouseService;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.LocalTCC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class WarehouseServiceImpl implements WarehouseService {

    private static final Logger logger = LoggerFactory.getLogger(WarehouseServiceImpl.class);

    private static final String OK_MSG = "OK";

    @Autowired
    JedisComponent jedisComponent;

    @Resource(name = "doDeduct")
    private DefaultRedisScript<Boolean> doDeductRedisScript;

    @Resource(name = "cancelDeduct")
    private DefaultRedisScript<Boolean> cancelDeductRedisScript;

    /**
     * redis存储的库存表名
     */
    @Value("${redis-warehouse.warehouse-table-name}")
    private String warehouseName;

    /**
     * redis存储的冻结库存表名
     */
    @Value("${redis-warehouse.frozen-table-name}")
    private String frozenTableName;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public String deductWarehouseFromRedis(String commodityId, Integer count) throws Exception {
        Jedis jedis = null;
        try {
            //获取Jedis实例
            jedis = jedisComponent.getJedis();

            List<String> keys = Arrays.asList(commodityId, String.valueOf(count));

            Object result = jedis.eval(doDeductRedisScript.getScriptAsString(), keys, new ArrayList<>());
            if (OK_MSG.equals(String.valueOf(result))) {
                return OK_MSG;
            } else {
                throw new Exception(String.valueOf(result));
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    @Override
    public boolean doDeductFromRedis(BusinessActionContext context) {
        logger.info("事务{}提交成功", context.getXid());
        //清空冻结的库存表
        String commodityId = (String) context.getActionContext("commodityId");
        //获取Jedis实例
        Jedis jedis = jedisComponent.getJedis();
        jedis.hdel(frozenTableName, commodityId);
        return true;
    }

    @Override
    public boolean cancelDeductFromRedis(BusinessActionContext context) throws Exception {

        String commodityId = (String) context.getActionContext("commodityId");
        List<String> keys = Arrays.asList(commodityId);
        //获取Jedis实例
        Jedis jedis = jedisComponent.getJedis();
        Object result = jedis.eval(cancelDeductRedisScript.getScriptAsString(), keys, new ArrayList<>());
        return true;
    }
}