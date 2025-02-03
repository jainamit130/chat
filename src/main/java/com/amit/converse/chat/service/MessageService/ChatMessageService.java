package com.amit.converse.chat.service.MessageService;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.context.ChatContext;
import com.amit.converse.chat.dto.Notification.MessageNotification;
import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.Messages.NotificationMessage;
import com.amit.converse.chat.repository.Message.IChatMessageRepository;
import com.amit.converse.chat.service.MessageProcessor.MessageProcessingService;
import com.amit.converse.chat.service.Notification.ChatNotificationService;
import com.amit.converse.chat.service.User.UserChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public abstract class ChatMessageService<T extends IChatRoom> {

    @Autowired
    protected ChatContext<T> chatContext;
    @Autowired
    protected MessageProcessingService messageProcessingService;
    @Autowired
    protected ChatNotificationService notificationService;
    @Autowired
    private UserChatService userChatService;
    @Autowired
    private IChatMessageRepository chatMessageRepository;

    public ChatMessage saveMessage(ChatMessage message) {
        return chatMessageRepository.save(message);
    }

    public List<ChatMessage> getMessagesOfChatFrom(String chatRoomId, String userId, Instant from) {
        return chatMessageRepository.findMessagesOfChatForUserFrom(chatRoomId,userId,from);
    }

    protected abstract void authoriseSender() throws ConverseException;

    public final void sendMessage(ChatMessage message) throws InterruptedException {
        T chatRoom = chatContext.getChatRoom();
        authoriseSender();
        ChatMessage savedMessage = saveMessage(message);
        notificationService.sendNotification(chatRoom.getId(),new MessageNotification(savedMessage));
        userChatService.connectChat(new ArrayList<>(chatRoom.getDeletedForUsers()));
        messageProcessingService.processMessageAfterSave(chatContext.getChatRoom().getId());
    }
}
