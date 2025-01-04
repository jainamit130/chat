package com.amit.converse.chat.service.MessageService;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.Messages.Message;
import com.amit.converse.chat.repository.Message.IMessageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DeleteMessageService {
    private final IMessageRepository messageRepository;

    public void deleteMessagesForUserFromTillNow(IChatRoom chatRoom, Instant from, String userId) {
        List<Message> messages = messageRepository.findMessagesOfChatForUserFrom(chatRoom.getId(), userId, from);
        List<Message> messagesToSave = new ArrayList<>();

        for (Message message : messages) {
            message.deleteMessage(userId);
            if (message.getDeletedForMembersCount() == chatRoom.getTotalMemberCount()) {
                messageRepository.deleteById(message.getId());
            } else {
                messagesToSave.add(message);
            }
        }

        if (!messagesToSave.isEmpty()) {
            messageRepository.saveAll(messagesToSave);
        }
    }
}
