package com.amit.converse.chat.service.MessageProcessor;

import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.Messages.Message;
import com.amit.converse.chat.model.User;

public interface deliveryProcessor {
    void deliver(User user);
    void deliver(ChatMessage message);
}
