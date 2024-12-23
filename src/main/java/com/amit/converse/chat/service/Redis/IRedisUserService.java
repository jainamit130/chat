package com.amit.converse.chat.service.Redis;

public interface IRedisUserService {
    void setUser(String userId);
    void removeUser(String userId);
}
