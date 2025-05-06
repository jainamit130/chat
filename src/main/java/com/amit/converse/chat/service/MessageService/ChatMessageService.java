package com.amit.converse.chat.service.MessageService;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.context.User.UserContext;
import com.amit.converse.chat.dto.Notification.MessageMarkedNotification;
import com.amit.converse.chat.dto.Notification.MessageNotification;
import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.model.Enums.MessageStatus;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.Message.IChatMessageRepository;
import com.amit.converse.chat.service.chatRoom.ChatService;
import com.amit.converse.chat.service.MessageProcessor.MessageProcessingService;
import com.amit.converse.chat.service.Notification.ChatNotificationService;
import com.amit.converse.chat.service.Notification.UserNotificationService;
import com.amit.converse.chat.service.User.UserChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChatMessageService<T extends IChatRoom> {

    @Autowired
    @Lazy
    protected UserChatService userChatService;
    @Autowired
    @Lazy
    protected ChatService chatService;
    @Autowired
    protected MessageProcessingService messageProcessingService;
    @Autowired
    protected ChatNotificationService chatNotificationService;
    @Autowired
    protected UserNotificationService userNotificationService;

    @Autowired
    private IChatMessageRepository chatMessageRepository;

    private ChatMessage getEmptyMessage() {
        return new ChatMessage();
    }

    public ChatMessage saveMessage(ChatMessage message) {
        return chatMessageRepository.save(message);
    }

    private void fulfilMessage(ChatMessage message) {
        message.setTimestamp(Instant.now());
        message.setName(UserContext.getUser().getUsername());
        message.setChatRoomId(chatService.getContextChatRoom().getId());
        message.setSenderId(UserContext.getUserId());
        message.setStatus(MessageStatus.PENDING);
    }

    public synchronized void saveMessages(List<ChatMessage> messages) {
        chatMessageRepository.saveAll(messages);
    }

    public List<ChatMessage> getMessagesToBeMarked(IChatRoom chatRoom) {
        User user = UserContext.getUser();
        Instant fromInstant = chatRoom.getUserFetchStartTime(user.getUserId());
        return chatMessageRepository.findMessagesOfChatForUserFrom(chatRoom.getId(),user.getUserId(),fromInstant);
    }

    public List<ChatMessage> getMessagesToBeMarked(IChatRoom chatRoom, User user, List<MessageStatus> inStatus, List<MessageStatus> notInStatus) {
        return chatMessageRepository.findToBeMarkedMessages(chatRoom.getId(),user.getUserId(),inStatus,notInStatus);
    }

    protected void authoriseSender() throws ConverseException {
        // all direct send messages are valid. No block feature yet.
        return;
    };

    public void sendMessageMarkedNotification(String senderId,MessageMarkedNotification messageMarkedNotification) {
        userNotificationService.sendNotification(senderId, messageMarkedNotification);
    }

    public void sendMessageNotification(String chatRoomId, ChatMessage message) {
        chatNotificationService.sendNotification(chatRoomId,new MessageNotification(message));
    }

    public final void sendMessage(ChatMessage message) throws InterruptedException {
        fulfilMessage(message);
        IChatRoom chatRoom = chatService.getContextChatRoom();
        authoriseSender();
        ChatMessage savedMessage = saveMessage(message);
        sendMessageNotification(chatRoom.getId(),savedMessage);
        userChatService.connectChat(new ArrayList<>(chatRoom.getDeletedForUsers()),chatRoom);
        messageProcessingService.process(savedMessage);
        chatService.processSentMessage();
    }

    public ChatMessage getLatestMessage(IChatRoom chatRoom) {
        Optional<ChatMessage> latestMessage = chatMessageRepository.findLatestMessage(chatRoom.getId(),UserContext.getUserId());
        if(latestMessage.isPresent()) {
            return latestMessage.get();
        }
        return new ChatMessage();
    }
}
