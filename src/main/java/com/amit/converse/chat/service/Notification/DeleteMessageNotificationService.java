package com.amit.converse.chat.service.Notification;

import com.amit.converse.chat.dto.Notification.MessageDeleteNotification;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.service.chatRoom.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeleteMessageNotificationService {
    @Autowired
    private ChatNotificationService chatNotificationService;

    @Autowired
    private ChatService chatService;

    public void sendMessageDeletedNotification(List<ChatMessage> messages) {
        List<String> deletedMessageIds = messages.stream().map(message -> message.getId()).collect(Collectors.toList());
        chatNotificationService.sendNotification(chatService.getContextChatRoom().getId(), MessageDeleteNotification.builder().messageIds(deletedMessageIds).build());
    }
}
