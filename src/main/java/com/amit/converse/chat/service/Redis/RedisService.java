package com.amit.converse.chat.service.Redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RedisService {

    @Autowired
    @Lazy
    private RedisTemplate<String,String> redisTemplate;

    public Boolean hasKeyValue(String keyValue) {
        return redisTemplate.hasKey(keyValue);
    }

    protected String getValueOfKey(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    protected void setKeyValue(String keyValue) {
        redisTemplate.opsForValue().set(keyValue,"");
    }

    public void removeKeyValue(String keyValue) {
        redisTemplate.delete(keyValue);
    }

    public void removeKeyValues(List<String> keyValues) {
        redisTemplate.delete(keyValues);
    }

    public List<String> getAllKeyValuesWithPrefix(String prefixKey) {
        return new ArrayList<>(redisTemplate.keys(prefixKey+"*"));
    }
}
