package com.amit.converse.chat.Redis;

import com.amit.converse.chat.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class RedisSession {
    @Autowired
    protected RedisService redisService;
    protected String userId;
    protected String prevChatRoomId;

    public RedisSession(String userId, String prevChatRoomId) {
        this.userId = userId;
        this.prevChatRoomId = prevChatRoomId;
    }
}
