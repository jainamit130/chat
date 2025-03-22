package com.amit.converse.chat.config.filter;


import com.amit.converse.chat.context.User.OfflineSetUserContextService;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.User.UserService;
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

    private final OfflineSetUserContextService userContext;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (request.getRequestURI().startsWith("/ws")) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            User user = userService.getLoggedInUser();
            userContext.setUser(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        filterChain.doFilter(request, response);
        userContext.clearContext();
    }
}
