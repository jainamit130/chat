package com.amit.converse.chat.config.Redis;

import com.amit.converse.chat.context.UserContext;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.Redis.RedisWriteService;
import com.amit.converse.chat.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class RedisKeyExpirationListener implements MessageListener {

    @Autowired
    @Lazy
    private UserService userService;

    @Autowired
    @Lazy
    private UserContext userContext;

    @Autowired
    @Lazy
    private RedisWriteService redisWriteService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String key = new String(message.getBody());
        String userId = redisWriteService.extractUserIdFromUserKey(key);
        if(userId!=null) {
            User user = userService.getUserById(userId);
            user.setState(userContext.getOnlineState(user));
            userContext.setUser(user);
            user.getState().transit();
        }
    }
}
