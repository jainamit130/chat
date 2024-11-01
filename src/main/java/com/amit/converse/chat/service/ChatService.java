package com.amit.converse.chat.service;

import com.amit.converse.chat.dto.MessageInfoDto;
import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.model.MessageStatus;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatService {

    private static final Duration DELETE_FOR_EVERYONE_LIMIT = Duration.ofMinutes(2);
    private final MessageProcessingService messageProcessingService;
    private final ChatMessageRepository chatMessageRepository;
    private final UserService userService;
    private final WebSocketMessageService webSocketMessageService;

    public ChatMessage addMessage(String chatRoomId, ChatMessage message, Boolean isSync) throws InterruptedException {

        User user = userService.getUser(message.getSenderId());

        message.setTimestamp(Instant.now());
        message.setChatRoomId(chatRoomId);
        message.setUser(user);
        message.setMessageStatus(MessageStatus.PENDING);
        message.setDeletedForUsers(new HashSet<>());
        message.setDeliveredRecipients(new HashSet<>());
        message.setReadRecipients(new HashSet<>());

        ChatMessage savedMessage = chatMessageRepository.save(message);

        if(isSync){
            messageProcessingService.processMessageSync(chatRoomId);
        } else {
            messageProcessingService.processMessageAsync(chatRoomId);
        }

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

    public Boolean deleteForEveryone(String messageId, String userId) {
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(()-> new ConverseException("message does not exist!"));
        Instant deletionDeadline = message.getTimestamp().plus(DELETE_FOR_EVERYONE_LIMIT);
        if(userId==message.getSenderId() && !message.getDeletedForUsers().contains(userId) && Instant.now().isBefore(deletionDeadline)){
            message.setContent("This message was deleted");
            message.setDeletedForEveryone(true);
            chatMessageRepository.save(message);
            webSocketMessageService.sendDeletedMessageStatus(message.getChatRoomId(),messageId);
            return true;
        }
        return false;
    }

    public Boolean deleteForMe(String messageId, String userId) {
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new ConverseException("message does not exist!"));
        Set<String> deletedForUsers = message.getDeletedForUsers();
        if(!deletedForUsers.contains(userId)){
            deletedForUsers.add(userId);
            message.setDeletedForUsers(deletedForUsers);
            chatMessageRepository.save(message);
            return true;
        }
        return false;
    }
}
