package com.amit.converse.chat.config.filter;

import com.amit.converse.chat.config.UserDetailsImpl;
import com.amit.converse.chat.model.Enums.Role;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.AuthService;
import com.amit.converse.chat.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class ChatRoomRoleFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String chatRoomId = request.getParameter("chatRoomId");
        if(chatRoomId!=null && AuthService.isAuthenticated()) {
            String userId = authService.getLoggedInUserId();
            if(userId!=null) {
                User user = userService.getUser();
                Set<String> adminChatRooms = user.getAdminRoleChatRoomIds();
                boolean isAdmin = adminChatRooms.contains(chatRoomId);
                Collection<GrantedAuthority> authorities = isAdmin
                        ? List.of(new SimpleGrantedAuthority(Role.ADMIN.toString()))
                        : List.of(new SimpleGrantedAuthority(Role.USER.toString()));
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        user, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
