package com.amit.converse.chat.State;

import com.amit.converse.chat.model.Enums.ConnectionStatus;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.MessageProcessor.DeliveryProcessingService;
import com.amit.converse.chat.service.Redis.Factory.RedisSessionTransitionFactory;
import com.amit.converse.chat.service.User.UserService;

public class Online extends State {

    @Override
    public ConnectionStatus getConnectionStatus() {
        return ConnectionStatus.ACTIVE;
    }

    public Online(User user, UserService userService, DeliveryProcessingService deliveryProcessingService, RedisSessionTransitionFactory redisSessionTransitionFactory) {
        super(user,userService,deliveryProcessingService,redisSessionTransitionFactory);
        user.setRedisSessionTransition(redisSessionTransitionFactory.getOfflineRedisSessionTransition());
    }

    @Override
    public void transit() {
        user.updateLastSeenToNow();
        userService.transit(user);
        user.setState(new Offline(user,userService,deliveryProcessingService,redisSessionTransitionFactory));
    }
}
