package com.amit.converse.chat.Redis;

public abstract class RedisSession {
    protected String userId;
    protected String prevChatRoomId;

    public RedisSession(String userId, String prevChatRoomId) {
        this.userId = userId;
        this.prevChatRoomId = prevChatRoomId;
    }
}
