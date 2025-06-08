package com.amit.converse.chat.service.MessageService.DeleteMessageService;

import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.repository.Message.IChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeleteChatMessageService extends DeleteMessageService {

    @Autowired
    protected IChatMessageRepository messageRepository;

    protected void saveDeletedMessages(List<ChatMessage> messagesToSave) {
        if (!messagesToSave.isEmpty()) {
            messageRepository.saveAll(messagesToSave);
        }
    }
}
