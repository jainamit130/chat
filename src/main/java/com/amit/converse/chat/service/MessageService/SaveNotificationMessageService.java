package com.amit.converse.chat.service.MessageService;

import com.amit.converse.chat.model.Messages.Message;
import com.amit.converse.chat.model.Messages.NotificationMessage;
import com.amit.converse.chat.repository.Message.INotificationMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaveNotificationMessageService {
    @Autowired
    private INotificationMessageRepository notificationMessageRepository;

    public Message saveMessage(NotificationMessage message) {
        return notificationMessageRepository.save(message);
    }
}
