package com.amit.converse.chat.config.util;

import com.amit.converse.chat.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Principal;
import java.util.Collections;

public class SecurityContextUtil {
    public static void ensureContextFromPrincipal(Principal principal) {
        if (principal instanceof UsernamePasswordAuthenticationToken auth) {
            populateSecurityContext(auth);
        } else {
            throw new IllegalStateException("Unsupported principal type: " + principal.getClass());
        }
    }

    public static User populateUserContext(User user) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user, null, Collections.emptyList()
        );
        populateSecurityContext(authenticationToken);
        return user;
    }

    private static void populateSecurityContext(UsernamePasswordAuthenticationToken authenticationToken) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authenticationToken);
        SecurityContextHolder.setContext(context);
    }
}
