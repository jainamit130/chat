package com.amit.converse.chat.Redis;

import com.amit.converse.chat.model.ConnectionStatus;
import com.amit.converse.chat.service.Redis.RedisWriteService;
import com.amit.converse.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

public abstract class RedisSessionTransition extends RedisSession implements Transition {
    @Autowired
    protected UserService userService;
    @Autowired
    protected RedisWriteService redisWriteService;
    private final ConnectionStatus status;

    RedisSessionTransition(ConnectionStatus status, String userId) {
        super(userId);
        this.status = status;
    }

    public final void transit() {
        alterUser();
        notifyStatusToChatRooms();
    }

    public abstract void alterUser();

    @Async
    public void notifyStatusToChatRooms() {
        userService.notifyStatus(userId,status);
    }
}
