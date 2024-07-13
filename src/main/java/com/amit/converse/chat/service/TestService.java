package com.amit.converse.chat.service;

import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.model.ChatRoom;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.ChatMessageRepository;
import com.amit.converse.chat.repository.ChatRoomRepository;
import com.amit.converse.chat.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public List<ChatMessage> getMessages(String chatRoomId) {
        return chatMessageRepository.findAllByChatRoomId(chatRoomId);
    }

    public void addMessage(String chatRoomId, ChatMessage message) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));

        User user = userRepository.findById(message.getSenderId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        message.setChatRoomId(chatRoom.getId());
        message.setUser(user);

        chatMessageRepository.save(message);
    }

    public ChatRoom createGroup(String groupName, String createdByUserId) {
        ChatRoom chatRoom = ChatRoom.builder()
                .name(groupName)
                .userIds(new ArrayList<>())
                .createdBy(createdByUserId)
                .createdAt(System.currentTimeMillis())
                .build();

        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoom addMembers(String chatRoomId, List<String> memberIds) {

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));

        chatRoom.getUserIds().addAll(memberIds);
        return chatRoomRepository.save(chatRoom);
    }
}