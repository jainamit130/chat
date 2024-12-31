package com.amit.converse.chat.context;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ChatContext {
    private String chatRoomId;
    private String userId;
}
