package com.amit.converse.chat.State;

import com.amit.converse.chat.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Offline extends State {

    @Autowired
    protected UserService userService;

    public Offline() {
        user.setRedisSessionTransition(redisSessionTransitionFactory.getOnlineRedisSessionTransition());
    }

    @Override
    public void transit() {
        user.setState(new Online());
        userService.transit();
        messageProcessingService.deliver();
    }
}
