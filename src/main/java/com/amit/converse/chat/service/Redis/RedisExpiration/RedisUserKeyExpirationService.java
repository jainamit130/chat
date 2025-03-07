package com.amit.converse.chat.service.Redis.RedisExpiration;

// user:{userId}:{chatRoomId} => update chatRoom and user context and transit both
// user:{userId}: => update user context and transit
public class RedisUserKeyExpirationService extends RedisExpirationService {
    @Override
    public void expire(String key) {

    }
}
