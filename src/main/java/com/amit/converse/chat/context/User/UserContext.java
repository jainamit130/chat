package com.amit.converse.chat.context.User;

import com.amit.converse.chat.context.IContext;
import com.amit.converse.chat.model.User;
import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class UserContext {
    private static final ThreadLocal<User> userContextHolder = new ThreadLocal<>();

    public static String getUserId() {
        User user = userContextHolder.get();
        return user != null ? user.getUserId() : null;
    }

    public static User getUser() {
        return userContextHolder.get();
    }

    public static void updateContext(User user) { userContextHolder.set(user); }

    public static void setUser(User user) {
        userContextHolder.set(user);
        userContextHolder.get().transit();
    }

    public static void clearContext() {
        userContextHolder.remove();
    }
}
