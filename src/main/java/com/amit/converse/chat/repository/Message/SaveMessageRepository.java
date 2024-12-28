package com.amit.converse.chat.repository.Message;

import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.model.NotificationMessage;

public interface SaveMessageRepository {

    void saveChatMessage(ChatMessage chatMessage);

    void saveNotificationMessage(NotificationMessage notificationMessage);
}
