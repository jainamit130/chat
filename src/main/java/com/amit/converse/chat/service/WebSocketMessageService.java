package com.amit.converse.chat.service;

import com.amit.converse.chat.dto.MessageStatusDto;
import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.model.ChatRoom;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class WebSocketMessageService {

    private final SimpMessagingTemplate messagingTemplate;
    private final Map<String, Set<String>> subscriptions = new ConcurrentHashMap<>();

    public int getSubscriptionCount(String chatRoomId) {
        return subscriptions.getOrDefault(chatRoomId, ConcurrentHashMap.newKeySet()).size();
    }

    public void sendTypingStatusToGroup(Set<String> typingUsers, String chatRoomId) {
        messagingTemplate.convertAndSend("/topic/typing/" + chatRoomId, typingUsers);
    }

    public void sendNewGroupStatusToMembers(String userId, ChatRoom savedChatRoom) {
        messagingTemplate.convertAndSend("/topic/user/" + userId, savedChatRoom);
    }

    public void sendMessage(String chatRoomId, ChatMessage chatMessage) {
        messagingTemplate.convertAndSend("/topic/chat/" + chatRoomId, chatMessage);
    }

    public void sendMarkedMessageStatus(String chatRoomId,String senderId,Boolean isDelivered){
        MessageStatusDto messageStatusDto = MessageStatusDto.builder().chatRoomId(chatRoomId).isDelivered(isDelivered).build();
        messagingTemplate.convertAndSend("/topic/message/"+senderId,messageStatusDto);
    }

    public void sendOnlineStatusToGroup(Set<String> onlineUsers, String chatRoomId) {
        messagingTemplate.convertAndSend("/online/"+chatRoomId,onlineUsers);
    }

    public void subscribeUser(String chatRoomId, String username) {
        subscriptions.computeIfAbsent(chatRoomId, k -> ConcurrentHashMap.newKeySet()).add(username);
    }

    public void unsubscribeUser(String chatRoomId, String username) {
        Set<String> users = subscriptions.get(chatRoomId);
        if (users != null) {
            users.remove(username);
            // Optionally remove the chat room from subscriptions if empty
            if (users.isEmpty()) {
                subscriptions.remove(chatRoomId);
            }
        }
    }
}