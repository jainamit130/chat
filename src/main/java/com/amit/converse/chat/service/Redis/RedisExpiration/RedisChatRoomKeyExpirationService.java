package com.amit.converse.chat.service.Redis.RedisExpiration;

import com.amit.converse.chat.service.Notification.InactiveChatRoomNotificationService;
import com.amit.converse.chat.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;

// chatRoomId:{chatRoomId}:{userId} => send inactiveChatRoomNotification to user
public class RedisChatRoomKeyExpirationService extends RedisExpirationService {

    @Autowired
    private UserService userService;

    @Autowired
    private InactiveChatRoomNotificationService inactiveChatRoomNotificationService;

    @Override
    public void expire(String key) {

    }
}
