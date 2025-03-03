package com.amit.converse.chat.service.MessageService.DeleteMessageService;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.Messages.Message;
import com.amit.converse.chat.repository.Message.IChatMessageRepository;
import com.amit.converse.chat.repository.Message.IMessageRepository;
import com.amit.converse.chat.service.Notification.DeleteMessageNotificationService;
import com.amit.converse.chat.service.User.UserChatService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class DeleteMessageService {

    @Autowired
    protected IChatMessageRepository messageRepository;
    @Autowired
    protected UserChatService userChatService;

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
