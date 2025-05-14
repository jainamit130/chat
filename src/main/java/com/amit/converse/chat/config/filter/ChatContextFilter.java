package com.amit.converse.chat.config.filter;

import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.service.chatRoom.ChatService;
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
public class ChatContextFilter extends OncePerRequestFilter {

    private final ChatService chatService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/ws")) {
            filterChain.doFilter(request, response);
            return;
        }
        String chatRoomId = null;
        if (requestURI.startsWith("/converse/chat")) {
            int lastIndex = requestURI.lastIndexOf("/");
            chatRoomId = requestURI.substring(lastIndex+1);
        }
        if (chatRoomId != null) {
            try {
                ChatRoom chatRoom = chatService.getChatRoomById(chatRoomId);
                if (chatRoom != null) {
                    chatService.updateChatRoomContext(chatRoom);
                }
            } catch (Exception e) {
            }
        }
        filterChain.doFilter(request, response);
    }

}
