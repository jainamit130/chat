package com.amit.converse.chat.Redis;

import com.amit.converse.chat.Interface.ITransition;
import com.amit.converse.chat.model.Enums.ConnectionStatus;
import com.amit.converse.chat.service.Redis.RedisWriteService;
import com.amit.converse.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

public abstract class RedisSessionITransition extends RedisSession implements ITransition {
    @Autowired
    protected UserService userService;
    @Autowired
    protected RedisWriteService redisWriteService;
    private final ConnectionStatus status;

    RedisSessionITransition(ConnectionStatus status, String userId) {
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
