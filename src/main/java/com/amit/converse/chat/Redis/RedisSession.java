package com.amit.converse.chat.Redis;

public abstract class RedisSession {
    protected String userId;

    public RedisSession(String userId) {
        this.userId = userId;
    }
}
