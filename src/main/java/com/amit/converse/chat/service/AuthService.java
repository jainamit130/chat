package com.amit.converse.chat.service;

import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null && authentication.getPrincipal() instanceof User) {
            return true;
        }
        return false;
    }

    public static User getUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public String getLoggedInUserId() {
        if(isAuthenticated()) {
            User user = getUser();
            String loggedInUserId = user.getUserId();
            if(loggedInUserId!=null) {
                return loggedInUserId;
            }
        }
        throw new ConverseException("User is not logged in!");
    }
}
