package com.amit.converse.chat.State;

import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.MessageProcessor.DeliveryProcessingService;
import com.amit.converse.chat.service.Redis.Factory.RedisSessionTransitionFactory;
import com.amit.converse.chat.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class StateFactoryService {

    @Autowired
    @Lazy
    private UserService userService;
    @Autowired
    private DeliveryProcessingService deliveryProcessingService;
    @Autowired
    private RedisSessionTransitionFactory redisSessionTransitionFactory;

    public State getState(User user) {
        if(user.getState()==null) {
            return getOfflineState(user);
        }
        return getOnlineState(user);
    }

    private Online getOnlineState(User user) {
        return new Online(user,userService,deliveryProcessingService,redisSessionTransitionFactory);
    }

    private Offline getOfflineState(User user) {
        return new Offline(user,userService,deliveryProcessingService,redisSessionTransitionFactory);
    }
}
