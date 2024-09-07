package com.amit.converse.chat.service;

import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.model.ChatRoom;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.ChatMessageRepository;
import com.amit.converse.chat.repository.ChatRoomRepository;
import com.amit.converse.chat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final RedisService redisService;

    public void addMessage(String chatRoomId, ChatMessage message) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));

        User user = userRepository.findByUserId(message.getSenderId());
        message.setTimestamp(Instant.now());
        message.setChatRoomId(chatRoom.getId());
        message.setUser(user);
        Set<String> onlineUserIds = redisService.filterOnlineUsers(chatRoom.getUserIds());
        message.setDeliveryReceiptsByTime(onlineUserIds);
        Set<String> onlineAndActiveUserIds = new HashSet<>();
        for (String userId : onlineUserIds) {
            if (redisService.isUserInChatRoom(chatRoomId,userId)) {
                onlineAndActiveUserIds.add(userId);
            }
        }
        message.setReadReceiptsByTime(onlineAndActiveUserIds);
        chatRoom.incrementTotalMessagesCount();
        chatRoomRepository.save(chatRoom);
        chatMessageRepository.save(message);
    }

    public void markAllMessagesDelivered(String userId){
        User user = userRepository.findByUserId(userId);
        List<String> chatRoomIds = user.getChatRoomIds();
        for(String chatRoomId: chatRoomIds){
            markAllMessagesReadOrDelivered(chatRoomId,userId,true);
        }
    }

    public void markAllMessagesRead(String chatRoomId,String userId){
        markAllMessagesReadOrDelivered(chatRoomId,userId,false);
    }

    public void markAllMessagesReadOrDelivered(String chatRoomId,String userId, Boolean isDelivered) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));
        chatRoom.allMessagesMarkedRead(userId);
        Integer unreadMessagesCount = chatRoom.getReadMessageCounts().get(userId);
        PageRequest pageRequest = PageRequest.of(0, unreadMessagesCount, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<ChatMessage> unreadMessages = chatRoomRepository.findUnreadMessagesByChatRoomId(chatRoomId,pageRequest);
        Instant timestamp = Instant.now();
        for (ChatMessage unreadMessage: unreadMessages){
            if(isDelivered){
                unreadMessage.addUserToDeliveredReceipt(timestamp,userId);
            } else {
                unreadMessage.addUserToReadReceipt(timestamp,userId);
            }
            chatMessageRepository.save(unreadMessage);
        }
        chatRoomRepository.save(chatRoom);
        return;
    }
}
