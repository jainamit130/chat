package com.amit.converse.chat.context.User;

import com.amit.converse.chat.State.State;
import com.amit.converse.chat.State.StateFactoryService;
import com.amit.converse.chat.context.IContext;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.Redis.RedisReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SetUserContextService implements IContext {
    @Autowired
    private StateFactoryService stateFactoryService;

    public State getState(User user) {
        return stateFactoryService.getState(user);
    }

    public void setUser(User user) {
        user.setState(getState(user));
        UserContext.setUser(user);
    }

    @Override
    public void clearContext() {
        UserContext.clearContext();
    }
}
