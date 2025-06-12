package com.amit.converse.chat.service.MessageService;

import com.amit.converse.chat.context.ChatRoom.ChatContext;
import com.amit.converse.chat.model.Enums.MessageStatus;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.Messages.Message;
import com.amit.converse.chat.model.Messages.NotificationMessage;
import com.amit.converse.chat.repository.Message.INotificationMessageRepository;
import com.amit.converse.chat.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class NotificationMessageService {

    @Autowired
    private INotificationMessageRepository notificationMessageRepository;

    public static NotificationMessage generateNotificationMessage(String chatRoomId,String content,Instant notificationTime) {
        NotificationMessage notificationMessage = new NotificationMessage();
        notificationMessage.setContent(content);
        notificationMessage.setChatRoomId(chatRoomId);
        notificationMessage.setTimestamp(notificationTime);
        return notificationMessage;
    }

    public NotificationMessage saveMessage(NotificationMessage message) {
        return notificationMessageRepository.save(message);
    }

    public NotificationMessage fulfilMessage(NotificationMessage message) {
        message.setChatRoomId(ChatContext.getChatRoomId());
        return message;
    }

}
