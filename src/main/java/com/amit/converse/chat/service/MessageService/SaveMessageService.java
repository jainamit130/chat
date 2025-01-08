package com.amit.converse.chat.service.MessageService;

import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.repository.Message.IMessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SaveMessageService {
    private final IMessageRepository messageRepository;

    public ChatMessage saveMessage(ChatMessage message) {
        return messageRepository.save(message);
    }
}
