package com.amit.converse.chat.service;

import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.model.ChatRoom;
import com.amit.converse.chat.model.MessageStatus;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final MessageProcessingService messageProcessingService;
    private final ChatMessageRepository chatMessageRepository;
    private final UserService userService;

    public ChatMessage addMessage(String chatRoomId, ChatMessage message) {

        User user = userService.getUser(message.getSenderId());

        message.setTimestamp(Instant.now());
        message.setChatRoomId(chatRoomId);
        message.setUser(user);
        message.setMessageStatus(MessageStatus.PENDING);

        ChatMessage savedMessage = chatMessageRepository.save(message);

        messageProcessingService.processMessageAfterSave(chatRoomId);

        return savedMessage;
    }
}
