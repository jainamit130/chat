package com.amit.converse.chat.service.MessageService;

import com.amit.converse.chat.model.Messages.NotificationMessage;
import com.amit.converse.chat.repository.Message.IMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;

public class NotificationMessageService {

    @Autowired
    private IMessageRepository messageRepository;

    public static NotificationMessage generateNotificationMessage(String chatRoomId,String content) {
        NotificationMessage notificationMessage = NotificationMessage.builder().build();
        notificationMessage.setContent(content);
        notificationMessage.setChatRoomId(chatRoomId);
        notificationMessage.setTimestamp(Instant.now());
        return notificationMessage;
    }

    public void saveMessage(NotificationMessage message) {
        messageRepository.save(message);
    }
}
