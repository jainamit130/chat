package com.amit.converse.chat.service;

import com.amit.converse.chat.dto.UserResponseDto;
import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.model.ChatRoom;
import com.amit.converse.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserService userService;
    private final RedisService redisService;
    private final SharedGroupChatService groupChatService;
    private final WebSocketMessageService webSocketMessageService;

    public List<ChatRoom> getChatRoomsOfUser(String userId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findByUserIdsContains(userId);
        for (ChatRoom chatRoom : chatRooms) {
            ChatMessage latestMessage = groupChatService.getLatestMessageOfGroup(chatRoom.getId());
            chatRoom.setUnreadMessageCount(chatRoom.getUnreadMessageCount(userId));
            chatRoom.setLatestMessage(latestMessage);
        }
        return chatRooms;
    }

    public Set<String> getOnlineUsersOfGroup(String chatRoomId){
        ChatRoom chatRoom = getChatRoom(chatRoomId);
        Set<String> onlineUserIds = getOnlineUsersOfGroup(chatRoom);
        return userService.processIdsToName(onlineUserIds);
    }

    public Set<String> getOnlineUsersOfGroup(ChatRoom chatRoom){
        return redisService.filterOnlineUsers(chatRoom.getUserIds());
    }

    public List<ChatMessage> getMessagesOfChatRoom(String chatRoomId,Integer startIndex){
        List<ChatMessage> chatMessages = groupChatService.getMessagesOfChatRoom(chatRoomId,startIndex,null);
        return chatMessages != null ? chatMessages : Collections.emptyList();
    }

    public ChatRoom createGroup(String groupName, String createdByUserId, List<String> memberIds) {

        Map<String, Integer> readMessageCounts = new HashMap<>();
        Map<String, Integer> deliverMessageCounts = new HashMap<>();

        ChatRoom chatRoom = ChatRoom.builder()
                .name(groupName)
                .userIds(memberIds)
                .readMessageCounts(readMessageCounts)
                .deliveredMessageCounts(deliverMessageCounts)
                .createdBy(createdByUserId)
                .totalMessagesCount(0)
                .createdAt(Instant.now())
                .build();

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        for (String userId : memberIds) {
            userService.groupJoinedOrLeft(userId,chatRoom.getId(),true);
            webSocketMessageService.sendNewGroupStatusToMembers(userId,savedChatRoom);
        }

        return savedChatRoom;
    }

    public ChatRoom addMembers(String chatRoomId, List<String> memberIds) {

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));
        for(String userId : memberIds){
            userService.groupJoinedOrLeft(userId,chatRoom.getId(),true);
        }
        chatRoom.getUserIds().addAll(memberIds);
        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoom getChatRoom(String chatRoomId){
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));
        return chatRoom;
    }

    public void saveChatRoom(ChatRoom chatRoom){
        chatRoomRepository.save(chatRoom);
        return;
    }

    public ChatRoom removeMembers(String chatRoomId, List<String> memberIds) {

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));
        for(String userId : memberIds){
            userService.groupJoinedOrLeft(userId,chatRoom.getId(),false);
        }
        chatRoom.getUserIds().removeAll(memberIds);
        return chatRoomRepository.save(chatRoom);
    }

    private static String getCurrentDateTimeAsString() {
        long currentTimeMillis = System.currentTimeMillis();
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(currentTimeMillis), ZoneOffset.UTC);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

    public List<ChatMessage> getMessagesToBeMarked(String chatRoomId, Integer toBeMarkedMessagesCount) {
        PageRequest pageRequest = PageRequest.of(0, toBeMarkedMessagesCount, Sort.by(Sort.Direction.DESC, "timestamp"));
        List<ChatMessage> messagesToBeMarked = groupChatService.getMessagesOfChatRoom(chatRoomId,pageRequest);
        return messagesToBeMarked;
    }
}