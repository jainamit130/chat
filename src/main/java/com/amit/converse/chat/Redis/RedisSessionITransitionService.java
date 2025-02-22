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
    private ConnectionStatus status;
    protected UserChatService userChatService;
    protected RedisWriteService redisWriteService;

    public RedisSessionITransitionService(ConnectionStatus status, UserChatService userChatService, RedisWriteService redisWriteService) {
        this.status = status;
        this.userChatService = userChatService;
        this.redisWriteService = redisWriteService;
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
