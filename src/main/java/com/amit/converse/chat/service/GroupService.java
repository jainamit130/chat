package com.amit.converse.chat.service;

import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.model.ChatRoom;
import com.amit.converse.chat.model.ChatRoomType;
import com.amit.converse.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatService chatService;
    private final UserService userService;
    private final RedisService redisService;
    private final SharedService sharedService;
    private final WebSocketMessageService webSocketMessageService;

    public List<ChatRoom> getChatRoomsOfUser(String userId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findByUserIdsContains(userId);
        for (ChatRoom chatRoom : chatRooms) {
            ChatMessage latestMessage = sharedService.getLatestMessageOfGroup(chatRoom.getId());
            chatRoom.setUnreadMessageCount(chatRoom.getUnreadMessageCount(userId));
            chatRoom.setLatestMessage(latestMessage);
        }
        return chatRooms;
    }

    public Set<String> getOnlineUsersOfGroup(String chatRoomId){
        ChatRoom chatRoom = sharedService.getChatRoom(chatRoomId);
        Set<String> onlineUserIds = getOnlineUsersOfGroup(chatRoom);
        return userService.processIdsToName(onlineUserIds);
    }

    public Set<String> getOnlineUsersOfGroup(ChatRoom chatRoom){
        return redisService.filterOnlineUsers(chatRoom.getUserIds());
    }

    public List<ChatMessage> getMessagesOfChatRoom(String chatRoomId,Integer startIndex){
        List<ChatMessage> chatMessages = sharedService.getMessagesOfChatRoom(chatRoomId,startIndex,null);
        return chatMessages != null ? chatMessages : Collections.emptyList();
    }

    public ChatRoom createGroup(String groupName, ChatRoomType chatRoomType, String createdById, List<String> memberIds, String message) {
        Map<String, Integer> readMessageCounts = new HashMap<>();
        Map<String, Integer> deliverMessageCounts = new HashMap<>();
        memberIds.add(createdById);

        ChatRoom chatRoom = ChatRoom.builder()
                .name(groupName)
                .userIds(memberIds)
                .chatRoomType(chatRoomType)
                .readMessageCounts(readMessageCounts)
                .deliveredMessageCounts(deliverMessageCounts)
                .createdBy(createdById)
                .totalMessagesCount(0)
                .createdAt(Instant.now())
                .build();

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        for (String userId : memberIds) {
            userService.groupJoinedOrLeft(userId,savedChatRoom.getId(),true);
            webSocketMessageService.sendNewGroupStatusToMembers(userId,savedChatRoom);
        }
        if(savedChatRoom.getChatRoomType().equals(ChatRoomType.INDIVIDUAL)){
            ChatMessage chatMessage = ChatMessage.builder().chatRoomId(savedChatRoom.getId()).content(message).senderId(createdById).build();
            chatService.addMessage(savedChatRoom.getId(),chatMessage);
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

    public ChatRoom removeMembers(String chatRoomId, List<String> memberIds) {

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));
        for(String userId : memberIds){
            userService.groupJoinedOrLeft(userId,chatRoom.getId(),false);
        }
        chatRoom.getUserIds().removeAll(memberIds);
        return chatRoomRepository.save(chatRoom);
    }
}