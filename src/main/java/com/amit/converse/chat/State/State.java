package com.amit.converse.chat.State;

import com.amit.converse.chat.Interface.ITransition;
import com.amit.converse.chat.model.Enums.ConnectionStatus;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.MessageProcessor.DeliveryProcessingService;
import com.amit.converse.chat.service.Redis.Factory.RedisSessionTransitionFactory;
import com.amit.converse.chat.service.User.UserService;

public abstract class State implements ITransition {
    protected User user;
    protected UserService userService;
    protected DeliveryProcessingService deliveryProcessingService;
    protected RedisSessionTransitionFactory redisSessionTransitionFactory;

    public abstract ConnectionStatus getConnectionStatus();

    public State(User user, UserService userService, DeliveryProcessingService deliveryProcessingService, RedisSessionTransitionFactory redisSessionTransitionFactory) {
        this.user = user;
        this.userService = userService;
        this.deliveryProcessingService = deliveryProcessingService;
        this.redisSessionTransitionFactory = redisSessionTransitionFactory;
    }
}

