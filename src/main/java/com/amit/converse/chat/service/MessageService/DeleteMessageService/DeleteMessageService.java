package com.amit.converse.chat.service.MessageService.DeleteMessageService;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.repository.Message.IChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class DeleteMessageService {

    @Autowired
    protected IChatMessageRepository messageRepository;

    protected void deleteMessageForUser(IChatRoom chatRoom, String userId, ChatMessage message, List<ChatMessage> messagesToSave) {
        message.deleteMessage(userId);
        if (message.getDeletedForMembersCount() == chatRoom.getTotalMemberCount()) {
            messageRepository.deleteById(message.getId());
        } else {
            messagesToSave.add(message);
        }
    }

    protected void saveDeletedMessages(List<ChatMessage> messagesToSave) {
        if (!messagesToSave.isEmpty()) {
            messageRepository.saveAll(messagesToSave);
        }
    }

    public void deleteMessagesForUserFromTillNow(IChatRoom chatRoom, Instant from, String userId) {
        List<ChatMessage> messages = messageRepository.findMessagesOfChatForUserFrom(chatRoom.getId(), userId, from);
        List<ChatMessage> messagesToSave = new ArrayList<>();
        for (ChatMessage message : messages) deleteMessageForUser(chatRoom,userId,message,messagesToSave);
        saveDeletedMessages(messagesToSave);
    }
}
