package com.amit.converse.chat.config.filter;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.Interface.ITransactable;
import com.amit.converse.chat.context.ChatContext;
import com.amit.converse.chat.model.Enums.Role;
import com.amit.converse.chat.service.AuthService;
import com.amit.converse.chat.service.ChatRoom.DirectChatService;
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
public class DirectChatFilter extends OncePerRequestFilter {
    private final ChatContext chatContext;
    private final DirectChatService directChatService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.startsWith("/converse/chat/direct/")) {
            String directChatId = request.getParameter("chatRoomId");
            IChatRoom directChat = directChatService.getChatRoomById(directChatId);
            if(directChat!=null) {
                chatContext.setChatRoom(directChat);
            }
        }
        filterChain.doFilter(request, response);
    }
}
