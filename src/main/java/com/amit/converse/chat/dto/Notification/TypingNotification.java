package com.amit.converse.chat.dto.Notification;

import lombok.Builder;

import java.util.List;

@Builder
public class TypingNotification implements IChatNotification {
    List<String> typingUsernames;
}
