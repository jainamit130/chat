package com.amit.converse.chat.service.Notification;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.dto.Notification.MessageDeleteNotification;
import com.amit.converse.chat.model.Messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeleteMessageNotificationService {
    @Autowired
    private ChatNotificationService chatNotificationService;

    public void sendMessageDeletedNotification(List<Message> messages, IChatRoom chatRoom) {
        for(Message message: messages) chatNotificationService.sendNotification(chatRoom.getId(), MessageDeleteNotification.builder().messageId(message.getId()).build());
    }
}
