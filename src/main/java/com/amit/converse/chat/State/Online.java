package com.amit.converse.chat.State;

import com.amit.converse.chat.Redis.OfflineRedisSessionITransitionService;

public class Online extends State {

    public Online() {
        user.setRedisSessionTransition(new OfflineRedisSessionITransitionService());
    }

    @Override
    public void transit() {
        user.setState(new Offline());
        userService.transit();
    }
}
