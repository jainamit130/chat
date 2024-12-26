package com.amit.converse.chat.State;

import com.amit.converse.chat.Redis.Transition;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.Redis.Factory.RedisSessionTransitionFactory;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class State implements Transition {
    @Autowired
    protected User user;
    @Autowired
    protected RedisSessionTransitionFactory redisSessionTransitionFactory;
}

