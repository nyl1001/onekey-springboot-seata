package com.example.coupon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

@Configuration
public class LuaConfiguration {
    @Bean(name = "initData")
    public DefaultRedisScript<Boolean> redisScriptInitData() {
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("luascript/initData.lua")));
        redisScript.setResultType(Boolean.class);
        return redisScript;
    }

    @Bean(name = "doDeduct")
    public DefaultRedisScript<Boolean> doDeductScript() {
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("luascript/doDeduct.lua")));
        redisScript.setResultType(Boolean.class);
        return redisScript;
    }

    @Bean(name = "cancelDeduct")
    public DefaultRedisScript<Boolean> cancelDeductScript() {
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("luascript/cancelDeduct.lua")));
        redisScript.setResultType(Boolean.class);
        return redisScript;
    }

    @Bean(name = "setLua")
    public DefaultRedisScript<Boolean> redisScriptSet() {
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("luascript/lockSetLua.lua")));
        redisScript.setResultType(Boolean.class);
        return redisScript;
    }

    @Bean(name = "delLua")
    public DefaultRedisScript<Boolean> redisScriptDel() {
        DefaultRedisScript<Boolean> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("luascript/lockDelLua.lua")));
        redisScript.setResultType(Boolean.class);
        return redisScript;
    }
}
