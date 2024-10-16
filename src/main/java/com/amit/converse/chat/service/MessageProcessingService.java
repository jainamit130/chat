package com.amit.converse.chat.service;

import com.amit.converse.chat.dto.OnlineStatusDto;
import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.model.ChatRoom;
import com.amit.converse.chat.model.OnlineStatus;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.ChatRoomRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;

@Service
@AllArgsConstructor
public class MessageProcessingService {

    private final ChatRoomRepository chatRoomRepository;
    private final RedisService redisService;
    private final UserService userService;
    private final WebSocketMessageService webSocketMessageService;
    private final MarkMessageService markMessageService;
    private final ObjectMapper objectMapper;
//    private final KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC_NAME = "chatMessages";

//    public void sendMessage(ChatMessage message) {
//        String serializedMessage = serializeMessage(message);
//        kafkaTemplate.send(TOPIC_NAME, serializedMessage);
//    }
//
//    @KafkaListener(topics = "chatMessages", groupId = "chat_group")
//    public void consumeMessage(String serializedMessage) {
//        ChatMessage message = deserializeMessage(serializedMessage);
//        processMessageAfterSave(message.getChatRoomId());
//    }
//
//    private String serializeMessage(ChatMessage message) {
//        try {
//            return objectMapper.writeValueAsString(message);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException("Failed to serialize message", e);
//        }
//    }
//
//    private ChatMessage deserializeMessage(String serializedMessage) {
//        try {
//            return objectMapper.readValue(serializedMessage, ChatMessage.class);
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to deserialize message", e);
//        }
//    }

    @Async
    public void processMessageAfterSave(String chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));
        chatRoom.incrementTotalMessagesCount();
        Set<String> onlineUserIds = redisService.filterOnlineUsers(chatRoom.getUserIds());
        for (String userId : onlineUserIds) {
            Integer toBeDeliveredMessagesCount=chatRoom.getUndeliveredMessageCount(userId);
            if(toBeDeliveredMessagesCount>0)
                markMessageService.markAllMessages(chatRoom, userId, true,toBeDeliveredMessagesCount);
            if (redisService.isUserInChatRoom(chatRoom.getId(), userId)) {
                Integer toBeReadMarkedMessagesCount=chatRoom.getUnreadMessageCount(userId);
                if(toBeReadMarkedMessagesCount>0)
                    markMessageService.markAllMessages(chatRoom,userId,false,toBeReadMarkedMessagesCount);
            }
        }
    }

    @Async
    public void sendOnlineStatusToAllChatRooms(String userId, OnlineStatus status){
        User user = userService.getUser(userId);
        OnlineStatusDto onlineStatusDto = OnlineStatusDto.builder().status(status).username(user.getUsername()).build();
        for(String chatRoomId: user.getChatRoomIds()){
            webSocketMessageService.sendOnlineStatusToGroup(chatRoomId,onlineStatusDto);
        }
        return;
    }
}
