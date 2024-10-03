package com.amit.converse.chat.service;

import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.model.ChatRoom;
import com.amit.converse.chat.model.ChatRoomType;
import com.amit.converse.chat.repository.ChatMessageRepository;
import com.amit.converse.chat.repository.ChatRoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@AllArgsConstructor
public class SharedService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

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

    public void createSelfChatRoom(String userId, String username){
        Map<String, Integer> readMessageCounts = new HashMap<>();
        Map<String, Integer> deliverMessageCounts = new HashMap<>();
        List<String> memberIds = new ArrayList<>();
        memberIds.add(userId);

        ChatRoom chatRoom = ChatRoom.builder()
                .name(username)
                .userIds(memberIds)
                .chatRoomType(ChatRoomType.SELF)
                .readMessageCounts(readMessageCounts)
                .deliveredMessageCounts(deliverMessageCounts)
                .createdBy(userId)
                .totalMessagesCount(0)
                .createdAt(Instant.now())
                .build();

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        return;
    }
}
