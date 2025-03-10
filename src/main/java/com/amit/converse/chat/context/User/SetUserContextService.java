package com.amit.converse.chat.context.User;

import com.amit.converse.chat.State.State;
import com.amit.converse.chat.State.StateFactoryService;
import com.amit.converse.chat.context.IContext;
import com.amit.converse.chat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SetUserContextService implements IContext {
    @Autowired
    protected StateFactoryService stateFactoryService;

    @Autowired
    private UserContext userContext;

    public State getState(User user) {
        return stateFactoryService.getOfflineState(user);
    }

    public void setUser(User user) {
        user.setState(getState(user));
        userContext.setUser(user);
    }

    @Override
    public void clearContext() {
        this.userContext.clearContext();
    }
}
