package com.amit.converse.chat.service;

import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.model.ChatRoom;
import com.amit.converse.chat.repository.ChatMessageRepository;
import com.amit.converse.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    public List<ChatRoom> getChatRoomsOfUser(String userId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findByUserIdsContains(userId);
        for (ChatRoom chatRoom : chatRooms) {
            ChatMessage latestMessage = chatMessageRepository.findTopByChatRoomIdOrderByTimestampDesc(chatRoom.getId());
            chatRoom.setLatestMessage(latestMessage);
        }
        return chatRooms != null ? chatRooms : Collections.emptyList(); // or throw exception if needed
    }

    public ChatMessage getLatestMessage(String chatRoomId) {
        return chatMessageRepository.findTopByChatRoomIdOrderByTimestampDesc(chatRoomId);
    }

    public List<ChatMessage> getMessagesOfChatRoom(String chatRoomId){
        List<ChatMessage> chatMessages = chatMessageRepository.findAllByChatRoomId(chatRoomId);
        return chatMessages != null ? chatMessages : Collections.emptyList();
    }
}
