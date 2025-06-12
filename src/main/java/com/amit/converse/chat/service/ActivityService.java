package com.amit.converse.chat.service;

import com.amit.converse.chat.dto.Notification.TypingNotification;
import com.amit.converse.chat.service.Notification.ChatNotificationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ActivityService {
    private final ChatNotificationService chatNotificationService;

    public void sendTypingNotification(String chatRoomId,List<String> typingUsernames) {
        TypingNotification typingNotification = TypingNotification.builder().typingUsernames(typingUsernames).build();
        chatNotificationService.sendNotification(chatRoomId,typingNotification);
    }
}
