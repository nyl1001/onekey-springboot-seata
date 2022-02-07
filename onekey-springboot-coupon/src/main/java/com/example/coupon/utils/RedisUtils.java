package com.example.coupon.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 *  redis 公共类
 *
 */
// 把普通pojo实例化到spring容器中
@Component
@Slf4j
public class  RedisUtils {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ValueOperations<String, String> valueOperations;

    @Autowired
    private HashOperations<String, String, String> hashOperations;

    @Autowired
    private ListOperations<String, Object> listOperations;

    @Autowired
    private SetOperations<String, Object> setOperations;

    @Autowired
    private ZSetOperations<String, Object> zSetOperations;

    @Autowired
    private ObjectMapper objectMapper; //序列化为json格式字符串

    /**
     *  object 转化为字符串
     * @param obj
     * @return
     */
    public String objectParseString(Object obj){
        String value = null;
        try {
            value = objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.info("object 转化为字符串 异常");
            e.printStackTrace();
        }
        return value;
    }

    /**
     *  把字符串转化为对象
     * @param str
     * @param var2
     * @return
     */
    public Object stringParseObject(String str, Object var2){
        Object object = null;
        try {
            object =objectMapper.readValue(str, var2.getClass());
        } catch (IOException e) {
            log.info("把字符串转化为对象 异常");
            e.printStackTrace();
        }
        return object;
    }

}
