package com.amit.converse.chat.service.MessageProcessor;

import com.amit.converse.chat.model.Messages.Message;
import com.amit.converse.chat.model.User;

public interface readProcessor {
    void read(User user);
    void read(Message message);
}
