package com.amit.converse.chat.Redis;

import com.amit.converse.chat.model.ConnectionStatus;
import com.amit.converse.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

public abstract class RedisSessionTransition extends RedisSession implements Transition {
    @Autowired
    protected UserService userService;

    private final ConnectionStatus status;

    RedisSessionTransition(ConnectionStatus status, String userId, String prevChatRoomId) {
        super(userId,prevChatRoomId);
        this.status = status;
    }

    public final void transit() {
        userService.updateUserLastSeen(userId);
        alterUserToChatRoom();
        alterUser();
        notifyStatusToChatRooms();
    }

    public abstract void alterUserToChatRoom();
    public abstract void alterUser();

    @Async
    public void notifyStatusToChatRooms() {
        userService.notifyStatus(userId,status);
    }
}
