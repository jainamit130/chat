package com.amit.converse.chat.State;

import com.amit.converse.chat.model.Enums.ConnectionStatus;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.MessageProcessor.DeliveryProcessingService;
import com.amit.converse.chat.service.Redis.Factory.RedisSessionTransitionFactory;
import com.amit.converse.chat.service.Redis.RedisReadService;
import com.amit.converse.chat.service.User.UserService;

public class Offline extends State {

    @Override
    public ConnectionStatus getConnectionStatus() {
        return ConnectionStatus.INACTIVE;
    }

    public Offline(User user, UserService userService, RedisReadService redisReadService, DeliveryProcessingService deliveryProcessingService, RedisSessionTransitionFactory redisSessionTransitionFactory) {
        super(user, userService, redisReadService, deliveryProcessingService,redisSessionTransitionFactory);
        user.setRedisSessionTransition(redisSessionTransitionFactory.getOnlineRedisSessionTransition());
    }

    @Override
    public void transit() {
        userService.transit(user);
        user.setState(new Online(user,userService,redisReadService,deliveryProcessingService,redisSessionTransitionFactory));
        deliveryProcessingService.deliver(user);
    }

    @Override
    public void commit() {

    }
}
