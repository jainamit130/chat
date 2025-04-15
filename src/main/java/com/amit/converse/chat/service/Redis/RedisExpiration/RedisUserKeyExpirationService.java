package com.amit.converse.chat.service.Redis.RedisExpiration;

import com.amit.converse.chat.context.User.OnlineSetUserContextService;
import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

// user:{userId}:{chatRoomId} => update chatRoom and user context and transit both
// user:{userId}: => update user context and transit
@Service
public class RedisUserKeyExpirationService extends RedisExpirationService {

    @Autowired
    @Lazy
    private OnlineSetUserContextService onlineSetUserContextService;

    @Autowired
    @Lazy
    private UserService userService;

    @Override
    public void expire(String keyValue) {
        try {
            String userId = redisKeyService.extractKey(keyValue);
            User user = userService.getUserById(userId);
            onlineSetUserContextService.setUser(user);
            onlineSetUserContextService.clearContext();
        } catch (ConverseException exception) {
            System.out.println("Invalid key found. Hence no user key to expire");
        }
    }
}
