package com.amit.converse.chat.repository.Message;

import com.amit.converse.chat.model.Messages.NotificationMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface INotificationMessageRepository extends MongoRepository<NotificationMessage,String> {
}
