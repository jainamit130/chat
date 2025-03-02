package com.amit.converse.chat.service.MessageService;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.Messages.Message;
import com.amit.converse.chat.repository.Message.IMessageRepository;
import com.amit.converse.chat.service.Notification.DeleteMessageNotificationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class DeleteMessageService implements IDeleteMessageService {
    private final IMessageRepository messageRepository;
    private final DeleteMessageNotificationService deleteMessageNotificationService;

    private void deleteMessageForUser(IChatRoom chatRoom, String userId, Message message, List<Message> messagesToSave) {
        message.deleteMessage(userId);
        if (message.getDeletedForMembersCount() == chatRoom.getTotalMemberCount()) {
            messageRepository.deleteById(message.getId());
        } else {
            messagesToSave.add(message);
        }
    }

    private void saveDeletedMessages(List<Message> messagesToSave) {
        if (!messagesToSave.isEmpty()) {
            messageRepository.saveAll(messagesToSave);
        }
    }

    public void deleteMessageForMe(IChatRoom chatRoom, String userId, List<Message> messages) {
        List<Message> messagesToSave = new ArrayList<>();
        for(Message message: messages) deleteMessageForUser(chatRoom,userId,message,messagesToSave);
        saveDeletedMessages(messagesToSave);
    }

    public void deleteMessageForEveryone(IChatRoom chatRoom, String userId, List<Message> messages) {
        List<Message> messagesToSave = new ArrayList<>();
        for(Message message: messages) {
        }
    }

    void deleteMessagesForUserFromTillNow(IChatRoom chatRoom, Instant from, String userId) {
        List<Message> messages = messageRepository.findMessagesOfChatForUserFrom(chatRoom.getId(), userId, from);
        List<Message> messagesToSave = new ArrayList<>();
        for (Message message : messages) deleteMessageForUser(chatRoom,userId,message,messagesToSave);
        saveDeletedMessages(messagesToSave);
    }
}
