package com.amit.converse.chat.controller;

//import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.service.UserServiceClient;
import com.amit.converse.common.ChatMessage;
import com.amit.converse.common.GetMessagesResponse;
import com.amit.converse.common.SendMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final UserServiceClient userServiceClient;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
//        ChatMessage grpcMessage = ChatMessage.builder()
//                .id(chatMessage.getId())
//                .sender(chatMessage.getSender())
//                .receiver(chatMessage.getReceiver())
//                .content(chatMessage.getContent())
//                .timeStamp(chatMessage.getTimeStamp())
//                .build();
        ChatMessage grpcMessage = ChatMessage.newBuilder()
                .setId(chatMessage.getId())
                .setSenderId(chatMessage.getSenderId())
                .setReceiverId(chatMessage.getReceiverId())
                .setContent(chatMessage.getContent())
                .setTimestamp(chatMessage.getTimestamp())
                .build();
        SendMessageResponse response = userServiceClient.sendMessage(grpcMessage);
        if (response.getSuccess()) {
            return chatMessage;
        } else {
            throw new RuntimeException("Failed to send message: " + response.getError());
        }
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSenderId());
        return chatMessage;
    }

    @MessageMapping("/chat.getMessages")
    @SendTo("/topic/public")
    public List<ChatMessage> getMessages(@Payload Map<String, String> payload) {
        String userId = payload.get("userId");
        String chatWithUserId = payload.get("chatWithUserId");
        GetMessagesResponse response = userServiceClient.getMessages(userId, chatWithUserId);
        return response.getMessagesList();
    }
}
