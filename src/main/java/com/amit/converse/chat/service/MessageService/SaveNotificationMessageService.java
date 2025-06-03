package com.amit.converse.chat.service.MessageService;

import com.amit.converse.chat.model.Messages.Message;
import com.amit.converse.chat.repository.Message.IMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaveMessageService {
    @Autowired
    private IMessageRepository messageRepository;

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }
}
