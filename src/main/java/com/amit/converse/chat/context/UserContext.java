package com.amit.converse.chat.context;

import com.amit.converse.chat.State.State;
import com.amit.converse.chat.State.StateFactoryService;
import com.amit.converse.chat.model.User;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Data
public class UserContext implements IContext {
    @Autowired
    private StateFactoryService stateFactoryService;
    private User user;

    public String getUserId() { return user.getUserId(); }

    public State getOnlineState(User user) { return stateFactoryService.getOnlineState(user); }

    public State getState(User user) {
        return stateFactoryService.getState(user);
    }

    public void setUser(User user) {
        this.user = user;
        this.user.setState(getState(this.user));
    }

    public void setAndTransitUser(User user) {
        setUser(user);
        this.user.transit();
    }

    @Override
    public void clearContext() {
        this.user = null;
    }
}
