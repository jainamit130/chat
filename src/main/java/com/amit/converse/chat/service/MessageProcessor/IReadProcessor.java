package com.amit.converse.chat.service.MessageProcessor;

import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.User;

public interface IReadProcessor {
    void read(User user);
    void read(ChatMessage message);
}
