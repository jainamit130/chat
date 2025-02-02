package com.amit.converse.chat.State;

import com.amit.converse.chat.Interface.ITransition;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.MessageProcessor.MessageProcessingService;
import com.amit.converse.chat.service.Redis.Factory.RedisSessionTransitionFactory;
import com.amit.converse.chat.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class State implements ITransition {
    @Autowired
    protected User user;

    @Autowired
    protected UserService userService;

    @Autowired
    protected MessageProcessingService messageProcessingService;

    @Autowired
    protected RedisSessionTransitionFactory redisSessionTransitionFactory;
}

