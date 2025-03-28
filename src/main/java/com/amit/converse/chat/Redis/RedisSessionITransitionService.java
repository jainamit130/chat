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

    public RedisSessionITransitionService(ConnectionStatus status, UserChatService userChatService, RedisWriteService redisWriteService, RedisReadService redisReadService) {
        this.status = status;
        this.userChatService = userChatService;
        this.redisWriteService = redisWriteService;
        this.redisReadService = redisReadService;
    }


    // Scenarios
    /*
    *
    * User is online and status is inactive - don't notify
    * User is online and status is active - notify
    * user is offline and status is active - not possible
    * user is offline and status is inactive - notify
    *
    * */
    private boolean isTransitable() {
        User user = userChatService.getContextUser();
        boolean isUserOnline = redisReadService.isUserOnline(user);
        ConnectionStatus status = user.getConnectionStatus();
        return (isUserOnline && status.equals(ConnectionStatus.ACTIVE)) || !isUserOnline;
    }


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
