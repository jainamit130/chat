package com.amit.converse.chat.State;

import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.MessageProcessor.DeliveryProcessingService;
import com.amit.converse.chat.service.Redis.Factory.RedisSessionTransitionFactory;
import com.amit.converse.chat.service.User.UserService;

public class Online extends State {

    public Online(User user, UserService userService, DeliveryProcessingService deliveryProcessingService, RedisSessionTransitionFactory redisSessionTransitionFactory) {
        super(user,userService,deliveryProcessingService,redisSessionTransitionFactory);
        user.setRedisSessionTransition(redisSessionTransitionFactory.getOfflineRedisSessionTransition());
    }

    @Override
    public void transit() {
        userService.transit();
        user.setState(new Offline(user,userService,deliveryProcessingService,redisSessionTransitionFactory));
    }
}
