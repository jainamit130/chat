package com.amit.converse.chat.service;

import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.repository.ChatMessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SharedGroupChatService {

    private final ChatMessageRepository chatMessageRepository;

    public List<ChatMessage> getAllMessagesOfChatRoom(String chatRoomId) {
        List<ChatMessage> messages =chatMessageRepository.findAllByChatRoomIdOrderByTimestampDesc(chatRoomId);
        return messages;
    }

    public List<ChatMessage> getMessagesOfChatRoom(String chatRoomId, Integer startIndex, Integer pageSize) {
        long totalMessages = chatMessageRepository.countByChatRoomId(chatRoomId);
        int offset = Math.min(startIndex, (int) totalMessages);

        Pageable pageable = PageRequest.of(offset, Integer.MAX_VALUE, Sort.by("timestamp").ascending());
        return chatMessageRepository.findMessagesWithPagination(chatRoomId, pageable);
    }

    public ChatMessage getLatestMessageOfGroup(String chatRoomId){
        ChatMessage latestMessage = chatMessageRepository.findTopByChatRoomIdOrderByTimestampDesc(chatRoomId);
        return latestMessage;
    }
}
