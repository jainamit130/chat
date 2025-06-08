package com.amit.converse.chat.service.MessageService.DeleteMessageService;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.Messages.Message;
import com.amit.converse.chat.repository.Message.IMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class DeleteMessageService {

    @Autowired
    protected IMessageRepository messageRepository;

    private void saveDeletedMessages(List<Message> messagesToSave) {
        if (!messagesToSave.isEmpty()) {
            messageRepository.saveAll(messagesToSave);
        }
    }

    protected void deleteMessageForUser(IChatRoom chatRoom, String userId, Message message, List<Message> messagesToSave) {
        message.deleteMessage(userId);
        if (message.getDeletedForMembersCount() == chatRoom.getTotalMemberCount()) {
            messageRepository.deleteById(message.getId());
        } else {
            messagesToSave.add(message);
        }
    }

    public void deleteMessagesForUserFromTillNow(IChatRoom chatRoom, Instant from, String userId) {
        List<Message> messages = messageRepository.findMessagesOfChatForUserFrom(chatRoom.getId(), userId, from);
        List<Message> messagesToSave = new ArrayList<>();
        for (Message message : messages) deleteMessageForUser(chatRoom,userId,message,messagesToSave);
        saveDeletedMessages(messagesToSave);
    }
}
