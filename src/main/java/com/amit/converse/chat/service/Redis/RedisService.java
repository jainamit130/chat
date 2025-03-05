package com.amit.converse.chat.service.Redis;

import com.amit.converse.chat.exceptions.ConverseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public abstract class RedisService {

    @Value("${Redis.Key.Timeout}")
    private long redisExpiryDuration;

    protected RedisTemplate<String,String> redisTemplate;

    public RedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    protected abstract String getPrefix();

    protected abstract String getKey(String keyValue);

    protected abstract String getKeyValue(String keyValue, String value);

    public String extractValue(String keyValue) {
        int lastIndexOfDelimiter = keyValue.lastIndexOf(':');
        if(keyValue.length()<=lastIndexOfDelimiter+1) throw new ConverseException("No Value found in Redis keyValue: "+keyValue);
        return keyValue.substring(lastIndexOfDelimiter+1);
    }

    protected void setKeyValue(String key, String value) {
        redisTemplate.opsForValue().set(getKey(key),value,redisExpiryDuration,TimeUnit.SECONDS);
    }

    // To already existing userKey add chatRoom as value => from userId:{userId}: to userId:{userId}:{chatRoomId}
    // To chatRoom add user as value => from chatRoomId:{chatRoomId}: to chatRoomId:{chatRoomId}:{userId}
    public void addValueToKey(String key,String value) {
        setKeyValue(key,value);
    }

    public void removeKeyValue(String keyValue,String value) {
        redisTemplate.delete(getKeyValue(keyValue,value));
    }

    public List<String> getAllKeyValuesWithPrefix(String prefixKey) {
        return new ArrayList<>(redisTemplate.keys(prefixKey));
    }
}
