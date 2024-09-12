package com.amit.converse.chat.service;

import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.repository.ChatMessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SharedGroupChatService {

    private final ChatMessageRepository chatMessageRepository;

    public List<ChatMessage> getAllMessagesOfGroup(String chatRoomId) {
        List<ChatMessage> messages =chatMessageRepository.findAllByChatRoomId(chatRoomId);
        return messages;
    }

    public List<ChatMessage> getAllMessagesOfGroup(String chatRoomId, PageRequest pageRequest) {
        List<ChatMessage> messages =chatMessageRepository.findAllByChatRoomId(chatRoomId);
        return messages;
    }

    public ChatMessage getLatestMessageOfGroup(String chatRoomId){
        ChatMessage latestMessage = chatMessageRepository.findTopByChatRoomIdOrderByTimestampDesc(chatRoomId);
        return latestMessage;
    }
}
