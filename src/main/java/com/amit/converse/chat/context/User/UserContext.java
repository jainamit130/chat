package com.amit.converse.chat.context.User;

import com.amit.converse.chat.model.Enums.ConnectionStatus;
import com.amit.converse.chat.model.User;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class UserContext {
    private static User USER_CONTEXT = null;

    public static String getUserId() {
        User user = getUser();
        return user != null ? user.getUserId() : null;
    }

    public static User getUser() {
        return USER_CONTEXT;
    }

    public static void updateContext(User user) { USER_CONTEXT = user; }

    public static void setUser(User user) {
        updateContext(user);
    }

    public static void clearContext() {
        USER_CONTEXT=null;
    }
}
