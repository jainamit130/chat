package com.amit.converse.chat.service;

import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.repository.ChatMessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class SharedGroupChatService {

    private final ChatMessageRepository chatMessageRepository;

    public List<ChatMessage> getMessagesOfChatRoom(String chatRoomId, Pageable pageable) {
        List<ChatMessage> messages =chatMessageRepository.findMessagesWithPagination(chatRoomId,pageable);
        return messages;
    }

    public List<ChatMessage> getMessagesOfChatRoom(String chatRoomId, Integer startIndex, Integer pageSize) {
        long totalMessages = chatMessageRepository.countByChatRoomId(chatRoomId);
        int offset = Math.min(startIndex, (int) totalMessages);
        int remainingMessages = (int) totalMessages - offset;

        if (remainingMessages <= 0) {
            return Collections.emptyList();
        }

        if (pageSize == null || pageSize <= 0) {
            pageSize = remainingMessages;
        }

        Pageable pageable;
        if (pageSize > 0) {
            int pageNumber = offset / pageSize;
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by("timestamp").ascending());
        } else {
            return Collections.emptyList();
        }

        return chatMessageRepository.findMessagesWithPagination(chatRoomId, pageable);
    }

    public ChatMessage getLatestMessageOfGroup(String chatRoomId){
        ChatMessage latestMessage = chatMessageRepository.findTopByChatRoomIdOrderByTimestampDesc(chatRoomId);
        return latestMessage;
    }
}
