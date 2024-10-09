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
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@AllArgsConstructor
public class MessageProcessingService {

    private final ChatRoomRepository chatRoomRepository;
    private final RedisService redisService;
    private final UserService userService;
    private final WebSocketMessageService webSocketMessageService;
    private final MarkMessageService markMessageService;

//    private final KafkaTemplate<String, String> kafkaTemplate;
//    private final ObjectMapper objectMapper;
//    private static final String TOPIC_NAME = "chatMessages";
//
//    private static final Lock chatRoomLock = new ReentrantLock(); // Lock to prevent concurrent modifications
//
//    public void sendMessage(ChatMessage message) {
//        String serializedMessage = serializeMessage(message);
//        kafkaTemplate.send(TOPIC_NAME, serializedMessage);
//    }
//
//    @KafkaListener(topics = "chatMessages", groupId = "chat_group")
//    public void consumeMessage(String serializedMessage, Acknowledgment acknowledgment) {
//        ChatMessage message = deserializeMessage(serializedMessage);
//
//        chatRoomLock.lock();
//        try {
//            processMessageAfterSave(message);
//            acknowledgment.acknowledge();
//        } catch (Exception e) {
//            System.err.println("Failed to process message: " + e.getMessage());
//        } finally {
//            chatRoomLock.unlock();
//        }
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
    public void processMessageAfterSave(ChatMessage message) {
            ChatRoom chatRoom = chatRoomRepository.findById(message.getChatRoomId())
                    .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));
            chatRoom.incrementTotalMessagesCount();
            Set<String> onlineUserIds = redisService.filterOnlineUsers(chatRoom.getUserIds());
            for (String userId : onlineUserIds) {
                markMessageService.markOneMessage(chatRoom, userId, true,message);
                if (redisService.isUserInChatRoom(chatRoom.getId(), userId)) {
                    markMessageService.markOneMessage(chatRoom,userId,false,message);
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
