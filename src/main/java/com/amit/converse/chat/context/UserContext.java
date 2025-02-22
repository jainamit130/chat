package com.amit.converse.chat.context;

import com.amit.converse.chat.State.StateFactoryService;
import com.amit.converse.chat.model.User;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Data
public class UserContext {
    @Autowired
    private StateFactoryService stateFactoryService;
    private String userId;
    private User user;

    public void setUser(User user) {
        user.setState(stateFactoryService.getState(user));
        setUserId(user.getUserId());
        this.user = user;
    }
}
