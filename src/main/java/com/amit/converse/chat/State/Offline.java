package com.amit.converse.chat.State;

import com.amit.converse.chat.Redis.OnlineRedisSessionITransitionService;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Offline extends State {

    public Offline(User user) {
        super(user);
        user.setRedisSessionTransition(new OnlineRedisSessionITransitionService());
    }

    @Override
    public void transit() {
        user.setState(new Online(user));
        userService.transit();
        deliveryProcessingService.deliver(user);
    }
}
