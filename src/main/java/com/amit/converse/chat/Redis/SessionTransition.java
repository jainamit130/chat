package com.amit.converse.chat.Redis;

import com.amit.converse.chat.model.ConnectionStatus;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.RedisService;
import org.springframework.scheduling.annotation.Async;

public abstract class SessionTransition {
    protected User user;
    protected RedisService redisService;

    public final void sessionTransitionEvent(String userId, String prevChatRoomId, ConnectionStatus status) {
        if(prevChatRoomId!=null) {
            alterUserToChatRoom(userId,prevChatRoomId);
        }
        alterUser(userId);
        notifyStatusToChatRooms(status);
    }

    public abstract void alterUserToChatRoom(String userId,String prevChatRoomId);
    public abstract void alterUser(String userId);

    @Async
    public void notifyStatusToChatRooms(ConnectionStatus status) {
        user.notifyStatus(status);
    }

    private final void chatRoomSessionTransitionEvent(String userId,String chatRoomId,String prevChatRoomId) {
        if(prevChatRoomId!=null){
            redisService.removeUserFromChatRoom(prevChatRoomId,userId);
        }
        redisService.addUserIdToChatRoom(chatRoomId,userId);
    }
}
