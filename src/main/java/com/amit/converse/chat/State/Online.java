package com.amit.converse.chat.State;

import org.springframework.stereotype.Component;

@Component
public class Online extends State {

    public Online() {
        user.setRedisSessionTransition(redisSessionTransitionFactory.getOfflineRedisSessionTransition());
    }

    @Override
    public void transit() {
        user.setState(new Offline());
        userService.transit();
    }
}
