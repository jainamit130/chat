package com.amit.converse.chat.config.filter;


import com.amit.converse.chat.context.UserContext;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.AuthService;
import com.amit.converse.chat.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class UserContextFilter extends OncePerRequestFilter {

    private final UserContext userContext;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            User user = userService.getLoggedInUser();
            userContext.setUser(user);
        } catch (Exception e) {
        }
        filterChain.doFilter(request, response);
    }
}
