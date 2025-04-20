package com.amit.converse.chat.State;

import com.amit.converse.chat.context.User.UserContext;
import com.amit.converse.chat.model.Enums.ConnectionStatus;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.MessageProcessor.DeliveryProcessingService;
import com.amit.converse.chat.service.Redis.Factory.RedisSessionTransitionFactory;
import com.amit.converse.chat.service.Redis.RedisReadService;
import com.amit.converse.chat.service.User.UserService;

public class Online extends State {

    @Override
    public ConnectionStatus getConnectionStatus() {
        return ConnectionStatus.ACTIVE;
    }

    @Override
    public boolean isTransitable() {
        return !redisReadService.isUserOnline(user);
    }

    public Online(User user, UserService userService, RedisReadService redisReadService, DeliveryProcessingService deliveryProcessingService, RedisSessionTransitionFactory redisSessionTransitionFactory) {
        super(user,userService,redisReadService,deliveryProcessingService,redisSessionTransitionFactory);
        user.setRedisSessionTransition(redisSessionTransitionFactory.getOfflineRedisSessionTransition());
    }

    @Override
    public void transit() {
        user.updateLastSeenToNow();
        userService.transit(user);
        user.setState(new Offline(user,userService,redisReadService,deliveryProcessingService,redisSessionTransitionFactory));
    }

    @Override
    public void commit() {
        user.commit();
    }
}
