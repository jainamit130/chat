package com.amit.converse.chat.Redis;

import com.amit.converse.chat.Interface.ITransition;
import com.amit.converse.chat.context.UserContext;
import com.amit.converse.chat.model.Enums.ConnectionStatus;
import com.amit.converse.chat.service.Redis.RedisWriteService;
import com.amit.converse.chat.service.User.UserChatService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Data
public abstract class RedisSessionITransitionService implements ITransition {
    @Autowired
    protected UserChatService userChatService;
    @Autowired
    protected UserContext userContext;
    @Autowired
    protected RedisWriteService redisWriteService;
    private ConnectionStatus status;

    public RedisSessionITransitionService(ConnectionStatus status) {
        this.status = status;
    }

    public final void transit() {
        alterUser();
        notifyStatusToChatRooms();
    }

    public abstract void alterUser();

    @Async
    public void notifyStatusToChatRooms() {
        userChatService.notifyStatus(status);
    }
}
