package com.amit.converse.chat.service.Notification;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.dto.Notification.MessageDeleteNotification;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.Messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeleteMessageNotificationService {
    @Autowired
    private ChatNotificationService chatNotificationService;

    public void sendMessageDeletedNotification(List<ChatMessage> messages) {
        for(ChatMessage message: messages) chatNotificationService.sendNotification(message.getChatRoomId(), MessageDeleteNotification.builder().messageId(message.getId()).build());
    }
}
