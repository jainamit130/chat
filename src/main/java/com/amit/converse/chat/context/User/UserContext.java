package com.amit.converse.chat.context.User;

import com.amit.converse.chat.context.IContext;
import com.amit.converse.chat.model.User;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class UserContext implements IContext {
    protected User user;

    public String getUserId() { return user.getUserId(); }

    public void setUser(User user) {
        this.user = user;
        this.user.getState().transit();
    }

    @Override
    public void clearContext() {
        this.user = null;
    }
}
