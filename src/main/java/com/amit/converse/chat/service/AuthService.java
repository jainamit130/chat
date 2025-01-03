package com.amit.converse.chat.service;

import com.amit.converse.chat.config.UserDetailsImpl;
import com.amit.converse.chat.exceptions.ConverseException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            return true;
        }
        return false;
    }

    public String getLoggedInUserId() {
        if(isAuthenticated()) {
            UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String loggedInUserId = userDetails.getUserId();
            if(loggedInUserId!=null) {
                return loggedInUserId;
            }
        }
        throw new ConverseException("User is not logged in!");
    }
}
