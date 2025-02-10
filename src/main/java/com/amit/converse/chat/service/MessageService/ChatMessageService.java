package com.amit.converse.chat.service.MessageService;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.context.ChatContext;
import com.amit.converse.chat.dto.Notification.MessageMarkedNotification;
import com.amit.converse.chat.dto.Notification.MessageNotification;
import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.Message.IChatMessageRepository;
import com.amit.converse.chat.service.MessageProcessor.MessageProcessingService;
import com.amit.converse.chat.service.Notification.ChatNotificationService;
import com.amit.converse.chat.service.Notification.UserNotificationService;
import com.amit.converse.chat.service.User.UserChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatMessageService<T extends IChatRoom> {

    @Autowired
    protected ChatContext<T> chatContext;
    @Autowired
    protected MessageProcessingService messageProcessingService;
    @Autowired
    protected ChatNotificationService chatNotificationService;
    @Autowired
    protected UserNotificationService userNotificationService;
    @Autowired
    private UserChatService userChatService;
    @Autowired
    private IChatMessageRepository chatMessageRepository;

    public ChatMessage saveMessage(ChatMessage message) {
        return chatMessageRepository.save(message);
    }

    public void saveMessages(List<ChatMessage> messages) { chatMessageRepository.saveAll(messages); }

    public List<ChatMessage> getMessagesOfChatFrom(IChatRoom chatRoom) {
        User user = userChatService.getContextUser();
        Instant fromInstant = chatRoom.getUserFetchStartTime(user.getUserId());
        return getMessagesOfChatFrom(chatRoom,user,fromInstant);
    }

    public List<ChatMessage> getMessagesOfChatFrom(IChatRoom chatRoom, User user, Instant fromInstant) {
        return chatMessageRepository.findMessagesOfChatForUserFrom(chatRoom.getId(),user.getUserId(),fromInstant);
    }

    protected void authoriseSender() throws ConverseException {
        // all direct send messages are valid. No block feature yet.
        return;
    };

    public void sendMessageMarkedNotification(String chatRoomId, String senderId, List<String> messageIds) {
        userNotificationService.sendNotification(senderId, new MessageMarkedNotification(chatRoomId,messageIds));
    }

    public void sendMessageNotification(String chatRoomId, ChatMessage message) {
        chatNotificationService.sendNotification(chatRoomId,new MessageNotification(message));
    }

    public final void sendMessage(ChatMessage message) throws InterruptedException {
        IChatRoom chatRoom = chatContext.getChatRoom();
        authoriseSender();
        ChatMessage savedMessage = saveMessage(message);
        sendMessageNotification(chatRoom.getId(),savedMessage);
        userChatService.connectChat(new ArrayList<>(chatRoom.getDeletedForUsers()));
        messageProcessingService.process(message);
    }
}
