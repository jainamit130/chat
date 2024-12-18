package com.amit.converse.chat.service;

import com.amit.converse.chat.dto.MessageInfoDto;
import com.amit.converse.chat.dto.UserDetails;
import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.model.*;
import com.amit.converse.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatService {

    private static final Duration DELETE_FOR_EVERYONE_LIMIT = Duration.ofMinutes(15);
    private final MessageProcessingService messageProcessingService;
    private final ChatMessageRepository chatMessageRepository;
    private final UserService userService;
    private final SharedService sharedService;
    private final WebSocketMessageService webSocketMessageService;

    public ChatMessage addMessage(String chatRoomId, ChatMessage message, Boolean isSync) throws InterruptedException {

        User user = userService.getUser(message.getSenderId());

        message.setTimestamp(Instant.now());
        message.setChatRoomId(chatRoomId);
        message.setUser(user);
        message.setType(MessageType.MESSAGE);
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

        Map<String, Set<UserDetails>> deliveryReceiptsByTime = convertMapIdsToMapName(message.getDeliveryReceiptsByTime());
        Map<String, Set<UserDetails>> readReceiptsByTime = convertMapIdsToMapName(message.getReadReceiptsByTime());

        return MessageInfoDto.builder()
                .deliveryReceiptsByTime(deliveryReceiptsByTime)
                .readReceiptsByTime(readReceiptsByTime)
                .build();
    }

    private Map<String, Set<UserDetails>> convertMapIdsToMapName(Map<String, Set<String>> receiptIdsByTime) {
        Map<String, Set<UserDetails>> updatedMap = new HashMap<>();

        for (Map.Entry<String, Set<String>> entry : receiptIdsByTime.entrySet()) {
            String timestamp = entry.getKey();
            Set<UserDetails> userDetails = userService.processIdsToUserDetails(entry.getValue());
            updatedMap.put(timestamp, userDetails);
        }

        return updatedMap;
    }

    public Boolean deleteForEveryone(String messageId, String userId) {
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(()-> new ConverseException("message does not exist!"));
        Instant deletionDeadline = message.getTimestamp().plus(DELETE_FOR_EVERYONE_LIMIT);
        if(userId.equals(String.valueOf(message.getSenderId()))
                && !message.getDeletedForUsers().contains(userId) && Instant.now().isBefore(deletionDeadline)){
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
        ChatRoom chatRoom = sharedService.getChatRoom(message.getChatRoomId());
        Set<String> deletedForUsers = message.getDeletedForUsers();
        if(!deletedForUsers.contains(userId)){
            deletedForUsers.add(userId);
            message.setDeletedForUsers(deletedForUsers);
            if (message.getDeletedForUsers().size() == chatRoom.getUserIds().size()) {
                chatMessageRepository.deleteById(message.getId());
            }
            chatMessageRepository.save(message);
            return true;
        }
        return false;
    }
}
