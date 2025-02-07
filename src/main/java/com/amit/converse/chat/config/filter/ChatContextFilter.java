package com.amit.converse.chat.config.filter;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.context.ChatContext;
import com.amit.converse.chat.service.ChatRoom.ChatService;
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

    private final ChatContext chatContext;
    private final ChatService chatService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String chatRoomId = request.getParameter("chatRoomId");
        if (chatRoomId != null) {
            try {
                IChatRoom chatRoom = chatService.getChatRoomById(chatRoomId);
                if (chatRoom != null) {
                    chatContext.setChatRoom(chatRoom);
                }
            } catch (Exception e) {
            }
        }
        filterChain.doFilter(request, response);
    }

}
