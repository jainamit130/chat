package com.amit.converse.chat.Redis;

import com.amit.converse.chat.Interface.ITransition;
import com.amit.converse.chat.context.User.UserContext;
import com.amit.converse.chat.model.Enums.ConnectionStatus;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.Redis.RedisReadService;
import com.amit.converse.chat.service.Redis.RedisWriteService;
import com.amit.converse.chat.service.User.UserChatService;
import lombok.Data;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Data
public abstract class RedisSessionITransitionService implements ITransition {
    private final ConnectionStatus status;
    protected final UserChatService userChatService;
    protected final RedisWriteService redisWriteService;
    protected final RedisReadService redisReadService;

    public RedisSessionITransitionService(ConnectionStatus status, UserChatService userChatService, RedisWriteService redisWriteService, RedisReadService redisReadService) {
        this.status = status;
        this.userChatService = userChatService;
        this.redisWriteService = redisWriteService;
        this.redisReadService = redisReadService;
    }

    public abstract boolean isTransitable();

    public final void transit() {
        boolean isNotifiable = isTransitable();
        alterUser();
        if(isNotifiable) {
            notifyStatusToChatRooms();
        }
    }

    public abstract void alterUser();

    @Async
    public void notifyStatusToChatRooms() {
        userChatService.notifyStatus(status);
    }
}
