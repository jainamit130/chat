package com.amit.converse.chat.service.MessageService.DeleteMessageService;

import com.amit.converse.chat.context.User.UserContext;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.service.Notification.DeleteMessageNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class DeleteMessageForEveryoneService extends DeleteMessageService {

    @Value("${messageValidTimeDiffForDeletingForEveryone}")
    private int thresholdTimeDiff;

    @Autowired
    private DeleteMessageNotificationService deleteMessageNotificationService;

    private boolean checkMessageBelonging(ChatMessage message) {
        String senderId = message.getSenderId();
        String userId = UserContext.getUserId();
        return senderId.equals(userId);
    }

    private boolean checkMessageValidity(ChatMessage message) {
        Instant messageSentTimestamp = message.getTimestamp();
        long timeDiff = Duration.between(messageSentTimestamp, Instant.now()).toMillis();
        return timeDiff<=thresholdTimeDiff;
    }

    private final boolean checkValidityForDeletingMessageForEveryone(ChatMessage message) {
        return checkMessageBelonging(message) && checkMessageValidity(message);
    }

    public void deleteMessageForEveryone(List<String> messageIds) {
        List<ChatMessage> messages = messageRepository.findAllById(messageIds);
        List<ChatMessage> messagesToSave = new ArrayList<>();
        for(ChatMessage message: messages) {
            if(checkValidityForDeletingMessageForEveryone(message)) {
                message.deleteForEveryone();
                messagesToSave.add(message);
            }
        }
        saveDeletedMessages(messagesToSave);
        deleteMessageNotificationService.sendMessageDeletedNotification(messagesToSave);
    }
}
