package com.amit.converse.chat.State;

import com.amit.converse.chat.Interface.ITransition;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.Redis.Factory.RedisSessionTransitionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class State implements ITransition {
    @Autowired
    protected User user;
    @Autowired
    protected RedisSessionTransitionFactory redisSessionTransitionFactory;
}

