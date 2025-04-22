package com.amit.converse.chat.State;

import com.amit.converse.chat.Interface.ICommit;
import com.amit.converse.chat.Interface.ITransition;
import com.amit.converse.chat.context.User.UserContext;
import com.amit.converse.chat.model.Enums.ConnectionStatus;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.MessageProcessor.DeliveryProcessingService;
import com.amit.converse.chat.service.Redis.Factory.RedisSessionTransitionFactory;
import com.amit.converse.chat.service.Redis.RedisReadService;
import com.amit.converse.chat.service.User.UserService;

public abstract class State implements ITransition, ICommit {
    protected User user;
    protected RedisReadService redisReadService;
    protected UserService userService;
    protected DeliveryProcessingService deliveryProcessingService;
    protected RedisSessionTransitionFactory redisSessionTransitionFactory;

    public abstract ConnectionStatus getConnectionStatus();

    public State(User user, UserService userService, RedisReadService redisReadService, DeliveryProcessingService deliveryProcessingService, RedisSessionTransitionFactory redisSessionTransitionFactory) {
        this.user = user;
        this.userService = userService;
        this.redisReadService = redisReadService;
        this.deliveryProcessingService = deliveryProcessingService;
        this.redisSessionTransitionFactory = redisSessionTransitionFactory;
    }

    public final void process() {
        if(redisReadService.isUserOnline(UserContext.getUser())) commit();
        else transit();
    }
}

