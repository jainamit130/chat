package com.amit.converse.chat.State;

import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.MessageProcessor.DeliveryProcessingService;
import com.amit.converse.chat.service.Redis.Factory.RedisSessionTransitionFactory;
import com.amit.converse.chat.service.User.UserService;

public class Offline extends State {

    public Offline(User user, UserService userService, DeliveryProcessingService deliveryProcessingService, RedisSessionTransitionFactory redisSessionTransitionFactory) {
        super(user, userService, deliveryProcessingService,redisSessionTransitionFactory);
        user.setRedisSessionTransition(redisSessionTransitionFactory.getOnlineRedisSessionTransition());
    }

    @Override
    public void transit() {
        userService.transit(user);
        user.setState(new Online(user,userService,deliveryProcessingService,redisSessionTransitionFactory));
        deliveryProcessingService.deliver(user);
    }
}
