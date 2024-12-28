package com.amit.converse.chat.service.MessageService;

import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.model.ChatRoom;
import com.amit.converse.chat.model.Message;
import com.amit.converse.chat.repository.Message.IChatMessageRepository;
import com.amit.converse.chat.repository.Message.IMessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@AllArgsConstructor
public class DeleteMessageService {
    private final ChatRoom chatRoom;
    private final IMessageRepository messageRepository;

    public void deleteMessagesForUserFromTillNow(Instant from,String userId) {
        List<Message> messages = messageRepository.findMessagesOfChatForUserFrom(chatRoom.getId(), userId, from);
    }
}
