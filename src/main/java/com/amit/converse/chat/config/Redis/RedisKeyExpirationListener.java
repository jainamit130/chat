package com.amit.converse.chat.config.Redis;

import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.Redis.RedisWriteService;
import com.amit.converse.chat.service.User.UserChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class RedisKeyExpirationListener implements MessageListener {

    @Autowired
    private UserChatService userChatService;

    @Autowired
    @Lazy
    private RedisWriteService redisWriteService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = new String(message.getBody());
        User user = userChatService.getContextUser();
        String expectedKey = redisWriteService.getUserKey(user.getUserId());
        if(expectedKey.equals(expiredKey)) {
            user.getState().transit();
        }
    }
}
