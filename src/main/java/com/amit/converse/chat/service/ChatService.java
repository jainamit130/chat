package com.amit.converse.chat.service;

import com.amit.converse.chat.dto.MessageInfoDto;
import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.model.MessageStatus;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
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
        message.setDeliveredRecipients(new HashSet<>());
        message.setReadRecipients(new HashSet<>());

        ChatMessage savedMessage = chatMessageRepository.save(message);

        messageProcessingService.processMessageAfterSave(chatRoomId);

        return savedMessage;
    }

    public MessageInfoDto getMessageInfo(String messageId) {
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new ConverseException("message no longer exists"));

        Map<String, Set<String>> deliveryReceiptsByTime = convertMapIdsToMapName(message.getDeliveryReceiptsByTime());
        Map<String, Set<String>> readReceiptsByTime = convertMapIdsToMapName(message.getReadReceiptsByTime());

        return MessageInfoDto.builder()
                .deliveryReceiptsByTime(deliveryReceiptsByTime)
                .readReceiptsByTime(readReceiptsByTime)
                .build();
    }

    private Map<String, Set<String>> convertMapIdsToMapName(Map<String, Set<String>> receiptIdsByTime) {
        Map<String, Set<String>> updatedMap = new HashMap<>();

        for (Map.Entry<String, Set<String>> entry : receiptIdsByTime.entrySet()) {
            String timestamp = entry.getKey();
            Set<String> usernames = userService.processIdsToName(entry.getValue());
            updatedMap.put(timestamp, usernames);
        }

        return updatedMap;
    }
}
