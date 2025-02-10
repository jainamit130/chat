package com.amit.converse.chat.State;

import com.amit.converse.chat.Redis.OnlineRedisSessionITransitionService;
import com.amit.converse.chat.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class Offline extends State {

    @Autowired
    protected UserService userService;

    public Offline() {
        user.setRedisSessionTransition(new OnlineRedisSessionITransitionService());
    }

    @Override
    public void transit() {
        user.setState(new Online());
        userService.transit();
        deliveryProcessingService.deliver(user);
    }
}
