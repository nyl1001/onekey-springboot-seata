package com.example.coupon.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis配置
 * Redis 数据类型: String（字符串）  Hash（哈希）  List（列表）   Set（集合）   zset(sorted set：有序集合)
 */
@Configuration
public class RedisConfig {

    @Autowired
    private RedisConnectionFactory factory;

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private Integer redisPort;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }

    /**
     * Hash（哈希）
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public HashOperations<String, String, String> hashOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForHash();
    }

    /**
     * String（字符串）
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public ValueOperations<String, String> valueOperations(RedisTemplate<String, String> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    /**
     * List（列表）
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForList();
    }

    /**
     * Set（集合）
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public SetOperations<String, Object> setOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForSet();
    }

    /**
     * zset(sorted set：有序集合)
     *
     * @param redisTemplate
     * @return
     */
    @Bean
    public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForZSet();
    }

    @Bean
    public JedisPool jedisPool() {
        //jedis连接池的配置
        JedisPoolConfig config = new JedisPoolConfig();
        // 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
        config.setBlockWhenExhausted(true);
        // 设置的逐出策略类名, 默认DefaultEvictionPolicy(当连接超过最大空闲时间,或连接数超过最 大空闲连接数)
        config.setEvictionPolicyClassName("org.apache.commons.pool2.impl.DefaultEvictionPolicy");
        // 是否启用pool的jmx管理功能, 默认true
        config.setJmxEnabled(true);
        // MBean ObjectName = new
        // ObjectName("org.apache.commons.pool2:type=GenericObjectPool,name="
        // + "pool" + i); 默认为"pool", JMX不熟,具体不知道是干啥的...默认就好.
        config.setJmxNamePrefix("pool");
        // 是否启用后进先出, 默认true
        config.setLifo(true);
        // 最大空闲连接数, 默认8个
        config.setMaxIdle(8);
        // 最大连接数, 默认8个
        config.setMaxTotal(8);
        // 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,
        // 默认-1
        config.setMaxWaitMillis(-1);
        // 逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
        config.setMinEvictableIdleTimeMillis(1800000);
        // 最小空闲连接数, 默认0
        config.setMinIdle(0);
        // 每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
        config.setNumTestsPerEvictionRun(3);
        // 对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数
        // 时直接逐出,不再根据MinEvictableIdleTimeMillis判断 (默认逐出策略)
        config.setSoftMinEvictableIdleTimeMillis(1800000);
        // 在获取连接的时候检查有效性, 默认false
        config.setTestOnBorrow(false);
        // 在空闲时检查有效性, 默认false
        config.setTestWhileIdle(false);
        // 逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
        config.setTimeBetweenEvictionRunsMillis(-1);
        return new JedisPool(config, this.redisHost, this.redisPort);
    }
}
