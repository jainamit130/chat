package com.amit.converse.chat.service.Redis.Interface;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.User;

public interface IRedisUserService {
    // Set userKey without any chatRoom as value => from not existing to existing as userId:{userId}:
    // To already existing userKey remove chatRoom as value => from userId:{userId}:{chatRoomId} to userId:{userId}:
    void setUserKey(User user, IChatRoom chatRoom);

    // remove already existing userKey => from userId:{userId}:{...} to not existing
    void removeUserKey(User user);
}
