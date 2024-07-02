package com.amit.converse.chat.config;

import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.model.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Slf4j
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessageSendingOperations simpMessageSendingOperations;

    public void handleDisconnectListen (SessionDisconnectEvent event){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = headerAccessor.getSessionAttributes().get("username").toString();
        if(username!=null){
            log.info("User Disconnected: {}",username);
            var chatMessage = ChatMessage.builder()
                                        .type(MessageType.LEAVE)
                                        .sender(username)
                                        .build();

            simpMessageSendingOperations.convertAndSend("/topic/public",chatMessage);
        }
    }
}
