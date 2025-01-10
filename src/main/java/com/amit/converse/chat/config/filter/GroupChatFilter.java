package com.amit.converse.chat.config.filter;

import com.amit.converse.chat.Interface.ITransactable;
import com.amit.converse.chat.context.ChatContext;
import com.amit.converse.chat.model.ChatRooms.GroupChat;
import com.amit.converse.chat.model.Enums.Role;
import com.amit.converse.chat.service.AuthService;
import com.amit.converse.chat.service.ChatRoom.GroupService;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class GroupChatFilter extends OncePerRequestFilter {
    private final AuthService authService;
    private final ChatContext chatContext;
    private final GroupService groupService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.startsWith("/converse/chat/group/")) {
            String groupChatId = request.getParameter("chatRoomId");
            ITransactable groupChat = groupService.getGroupById(groupChatId);
            if(groupChat!=null) {
                chatContext.setChatRoom(groupChat);
                Set<String> adminUserIds = new HashSet<>(groupChat.getAdminUserIds());
                String userId = authService.getLoggedInUserId();
                if(userId!=null) {
                    Collection<GrantedAuthority> authorities;
                    if (adminUserIds.contains(userId)) {
                        authorities = List.of(new SimpleGrantedAuthority(Role.ADMIN.toString()));
                    } else {
                        authorities = List.of(new SimpleGrantedAuthority(Role.USER.toString()));
                    }
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userId, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
