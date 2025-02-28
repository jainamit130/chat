package com.amit.converse.chat.service.Redis;

public interface IRedisUserService extends IRedisService {
    void setUser(String userId);
    void removeUser(String userId);
}
