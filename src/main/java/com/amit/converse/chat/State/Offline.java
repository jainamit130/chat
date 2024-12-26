package com.amit.converse.chat.State;

import org.springframework.stereotype.Component;

@Component
public class Offline extends State {

    public Offline() {
        user.setRedisSessionTransition(redisSessionTransitionFactory.getOnlineRedisSessionTransition());
    }

    @Override
    public void transit() {
        user.setState(new Online());
        user.transit();
    }
}
