package com.amit.converse.chat.service;

import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.model.ChatRoom;
import com.amit.converse.chat.model.ChatRoomType;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.ChatMessageRepository;
import com.amit.converse.chat.repository.ChatRoomRepository;
import com.amit.converse.chat.repository.UserRepository;
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
    private final UserRepository userRepository;

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

    public User createUserAndSelfChatRoom(User user){
        Map<String, Integer> readMessageCounts = new HashMap<>();
        Map<String, Integer> deliverMessageCounts = new HashMap<>();
        List<String> memberIds = new ArrayList<>();
        memberIds.add(user.getUserId());

        ChatRoom chatRoom = ChatRoom.builder()
                .name(user.getUsername())
                .userIds(memberIds)
                .chatRoomType(ChatRoomType.SELF)
                .readMessageCounts(readMessageCounts)
                .deliveredMessageCounts(deliverMessageCounts)
                .createdBy(user.getUserId())
                .totalMessagesCount(0)
                .createdAt(Instant.now())
                .build();

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        User savedUser = userRepository.save(user);
        user.addChatRoom(savedChatRoom.getId());
        userRepository.save(user);
        return savedUser;
    }

    public List<ChatMessage> getMessagesToBeMarked(String chatRoomId, Integer toBeMarkedMessagesCount) {
        PageRequest pageRequest = PageRequest.of(0, toBeMarkedMessagesCount, Sort.by(Sort.Direction.DESC, "timestamp"));
        List<ChatMessage> messagesToBeMarked = getMessagesOfChatRoom(chatRoomId,pageRequest);
        return messagesToBeMarked;
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
}
