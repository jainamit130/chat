package com.amit.converse.chat.service.Redis;

import java.util.List;

public interface IRedisUserService extends IRedisService {
    void setUserKey(String key);
    void setUserChatRoomKey(String key);
    void removeUserChatRoomKey(String key);
    List<String> getAllKeysWithPrefix(String prefix);
}
