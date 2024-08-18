package com.amit.converse.chat.service;

import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.model.ChatRoom;
import com.amit.converse.chat.repository.ChatMessageRepository;
import com.amit.converse.chat.repository.ChatRoomRepository;
import com.amit.converse.chat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    public List<ChatRoom> getChatRoomsOfUser(String userId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findByUserIdsContains(userId);
        for (ChatRoom chatRoom : chatRooms) {
            ChatMessage latestMessage = chatMessageRepository.findTopByChatRoomIdOrderByTimestampDesc(chatRoom.getId());
            chatRoom.setLatestMessage(latestMessage);
        }
        return chatRooms != null ? chatRooms : Collections.emptyList(); // or throw exception if needed
    }

    public List<ChatMessage> getMessagesOfChatRoom(String chatRoomId){
        List<ChatMessage> chatMessages = chatMessageRepository.findAllByChatRoomId(chatRoomId);
        return chatMessages != null ? chatMessages : Collections.emptyList();
    }

    public ChatRoom createGroup(String groupName, String createdByUserId) {

        ChatRoom chatRoom = ChatRoom.builder()
                .name(groupName)
                .userIds(new ArrayList<>())
                .createdBy(createdByUserId)
                .createdAt(getCurrentDateTimeAsString())
                .build();

        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoom addMembers(String chatRoomId, List<String> memberIds) {

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));

        chatRoom.getUserIds().addAll(memberIds);
        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoom removeMembers(String chatRoomId, List<String> memberIds) {

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));

        chatRoom.getUserIds().removeAll(memberIds);
        return chatRoomRepository.save(chatRoom);
    }

    private static String getCurrentDateTimeAsString() {
        long currentTimeMillis = System.currentTimeMillis();
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(currentTimeMillis), ZoneOffset.UTC);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return dateTime.format(formatter);
    }

}