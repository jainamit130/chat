package com.amit.converse.chat.State;

import com.amit.converse.chat.Redis.OfflineRedisSessionITransitionService;
import com.amit.converse.chat.model.User;
import org.springframework.stereotype.Component;

@Component
public class Online extends State {

    public Online(User user) {
        super(user);
        user.setRedisSessionTransition(new OfflineRedisSessionITransitionService());
    }

    @Override
    public void transit() {
        user.setState(new Offline(user));
        userService.transit();
    }
}
