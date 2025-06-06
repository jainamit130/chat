package com.amit.converse.chat.service.MessageProcessor;

import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.User;

public interface IReadProcessor {
    void read(User user,MarkingContext context);
    void read(ChatMessage message,MarkingContext context);
}
