package com.amit.converse.chat.config.Redis;

import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.service.Redis.Factory.RedisKeyExpirationFactory;
import com.amit.converse.chat.service.Redis.RedisWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class RedisKeyExpirationListener implements MessageListener {

    @Autowired
    private RedisKeyExpirationFactory redisKeyExpirationFactory;

    @Autowired
    private RedisWriteService redisWriteService;

    // Three different things can expire
    // user:{userId}:{chatRoomId} => update chatRoom and user context and transit both
    // user:{userId}: => update user context and transit
    // chatRoomId:{chatRoomId}:{userId} => send inactiveChatRoomNotification to user
    @Override
    public void onMessage(Message message, byte[] pattern) {
        // Get key expiry notification
        String key = new String(message.getBody());
        try {
            // refresh the key and expire it
            redisWriteService.setKey(key);
            redisKeyExpirationFactory.getRedisExpirationService(key).expire(key);
        } catch (ConverseException exception) {
            System.out.println("No expiry service found because : "+exception.getMessage());
        }
    }
}
