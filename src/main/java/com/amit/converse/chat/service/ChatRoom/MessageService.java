package com.amit.converse.chat.service.ChatRoom;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.context.ChatContext;
import com.amit.converse.chat.dto.Notification.MessageNotification;
import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.service.MessageProcessingService;
import com.amit.converse.chat.service.MessageService.SaveMessageService;
import com.amit.converse.chat.service.Notification.ChatNotificationService;
import com.amit.converse.chat.service.User.UserChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public abstract class MessageService<T extends IChatRoom> {

    @Autowired
    protected ChatContext<T> chatContext;
    @Autowired
    protected MessageProcessingService messageProcessingService;
    @Autowired
    protected SaveMessageService saveMessageService;
    @Autowired
    protected ChatNotificationService notificationService;
    @Autowired
    private UserChatService userChatService;

    protected abstract void authoriseSender() throws ConverseException;

    public final void sendMessage(ChatMessage message) throws InterruptedException {
        T chatRoom = chatContext.getChatRoom();
        authoriseSender();
        ChatMessage savedMessage = saveMessageService.saveMessage(message);
        notificationService.sendNotification(chatRoom.getId(),new MessageNotification(savedMessage));
        userChatService.connectChat(new ArrayList<>(chatRoom.getDeletedForUsers()));
        messageProcessingService.processMessageAfterSave(chatContext.getChatRoom().getId());
    }
}
