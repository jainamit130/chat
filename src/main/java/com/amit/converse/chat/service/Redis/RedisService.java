package com.amit.converse.chat.service.Redis;

import com.amit.converse.chat.exceptions.ConverseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class RedisService {

    @Value("${Redis.Key.Timeout}")
    private long redisExpiryDuration;

    private RedisTemplate<String,String> redisTemplate;

    public RedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Boolean hasKeyValue(String keyValue) {
        return redisTemplate.hasKey(keyValue);
    }

    protected void setKeyValue(String key, String value) {
        redisTemplate.opsForValue().set(key,value,redisExpiryDuration,TimeUnit.SECONDS);
    }

    // To already existing userKey add chatRoom as value => from userId:{userId}: to userId:{userId}:{chatRoomId}
    // To chatRoom add user as value => from chatRoomId:{chatRoomId}: to chatRoomId:{chatRoomId}:{userId}
    public void addValueToKey(String key,String value) {
        setKeyValue(key,value);
    }

    public void removeKeyValue(String keyValue) {
        redisTemplate.delete(keyValue);
    }

    public void removeKeyValues(List<String> keyValues) {
        redisTemplate.delete(keyValues);
    }

    public List<String> getAllKeyValuesWithPrefix(String prefixKey) {
        return new ArrayList<>(redisTemplate.keys(prefixKey));
    }
}
