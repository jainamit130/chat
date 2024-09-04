package com.amit.converse.chat.exceptions;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class ConverseChatRoomNotFoundException extends UsernameNotFoundException {
    public ConverseChatRoomNotFoundException(String chatRoomName) {
        super("ChatRoom not found - " + chatRoomName);
    }

    public ConverseChatRoomNotFoundException() {
        super("ChatRoom not found");
    }
}
