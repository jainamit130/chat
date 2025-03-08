package com.amit.converse.chat.Redis;

import com.amit.converse.chat.Interface.ITransition;
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
    private ConnectionStatus status;
    protected UserChatService userChatService;
    protected RedisWriteService redisWriteService;
    private RedisReadService redisReadService;

    public RedisSessionITransitionService(ConnectionStatus status, UserChatService userChatService, RedisWriteService redisWriteService) {
        this.status = status;
        this.userChatService = userChatService;
        this.redisWriteService = redisWriteService;
    }

    private boolean isNotifiable() {
        User user = userChatService.getContextUser();
        return !redisReadService.isUserOnline(user) && user.getConnectionStatus().equals(ConnectionStatus.OFFLINE) ||
                !redisReadService.isUserOnline(user) && user.getConnectionStatus().equals(ConnectionStatus.ONLINE);
    }

    public final void transit() {
        alterUser();
        if(isNotifiable()) {
            notifyStatusToChatRooms();
        }
    }

    public abstract void alterUser();

    @Async
    public void notifyStatusToChatRooms() {
        userChatService.notifyStatus(status);
    }
}
