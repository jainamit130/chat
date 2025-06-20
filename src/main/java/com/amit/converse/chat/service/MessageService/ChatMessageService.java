package com.amit.converse.chat.service.MessageService;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.context.ChatRoom.ChatContext;
import com.amit.converse.chat.dto.MessageInfoDto;
import com.amit.converse.chat.dto.Notification.MessageMarkedNotification;
import com.amit.converse.chat.dto.Notification.MessageNotification;
import com.amit.converse.chat.dto.UserDetails;
import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.Enums.MessageStatus;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.Messages.Message;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.Message.IChatMessageRepository;
import com.amit.converse.chat.repository.Message.IMessageRepository;
import com.amit.converse.chat.service.User.UserService;
import com.amit.converse.chat.service.chatRoom.ChatService;
import com.amit.converse.chat.service.MessageProcessor.MessageProcessingService;
import com.amit.converse.chat.service.Notification.ChatNotificationService;
import com.amit.converse.chat.service.Notification.UserNotificationService;
import com.amit.converse.chat.service.User.UserChatService;
import com.amit.converse.chat.service.chatRoom.MessageFilters.MessageFilterFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class ChatMessageService<T extends IChatRoom> extends MessageService {

    @Autowired
    @Lazy
    protected UserChatService userChatService;
    @Autowired
    @Lazy
    private MessageFilterFactory messageFilterFactory;
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
    @Autowired
    private IMessageRepository messageRepository;

    public ChatMessage saveMessage(ChatRoom chatRoom,ChatMessage message) {
        updateLatestMessagesOfMembers(message,chatRoom,chatRoom.getAllUserIds());
        return chatMessageRepository.save(message);
    }

    private void fulfilMessage(ChatMessage message) {
        message.setTimestamp(Instant.now());
        message.setDeletedForEveryone(false);
        message.setName(UserService.getUserContext().getDisplayName());
        message.setChatRoomId(ChatContext.getChatRoomId());
        message.setSenderId(UserService.getUserContext().getUserId());
        message.setStatus(MessageStatus.PENDING);
        message.setMemberCount(ChatContext.getChatRoom().getMemberCount());
    }

    public void saveMessages(List<ChatMessage> messages) {
        chatMessageRepository.saveAll(messages);
    }

    public List<Message> getMessagesOfChatRoom(IChatRoom chatRoom) {
        User user = UserService.getUserContext();
        Instant fromInstant = chatRoom.getUserFetchStartTime(user.getUserId());
        return messageRepository.findReadOnlyMessagesOfChatForUserFrom(chatRoom.getId(),user.getUserId(),fromInstant);
    }

    public List<ChatMessage> getMessagesOfChatRoom(ChatRoom chatRoom, User user, Instant fromInstant) {
        return messageFilterFactory.getBlindPeriodFilter(chatRoom.getChatRoomType()).filterBlindSpots(chatRoom,user,chatMessageRepository.findMessagesOfChatForUserFrom(chatRoom.getId(),user.getUserId(),fromInstant));
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
        ChatRoom chatRoom = chatService.getContextChatRoom();
        authoriseSender();
        ChatMessage savedMessage = saveMessage(chatRoom,message);
        sendMessageNotification(chatRoom.getId(),savedMessage);
        messageProcessingService.processMessage(chatRoom,savedMessage);
    }

    public Message getLatestMessage(ChatRoom chatRoom,User user) {
        Optional<Message> optionalLatestMessage = chatRoom.getLatestMessage(user.getUserId());
        if(optionalLatestMessage.isPresent()) {
            Message latestMessage = optionalLatestMessage.get();
            latestMessage.processStatus(user.getUserId());
            return latestMessage;
        }
        return new ChatMessage(chatRoom.getUserFetchStartTime(user.getUserId()));
    }

    public void readMessage(ChatRoom chatRoom,User user) {
        chatService.readMessages(chatRoom,user);
    }

    public MessageInfoDto getMessageInfo(String messageId) {
        ChatMessage message = chatMessageRepository.findMessageById(messageId,UserService.getUserContext().getUserId())
                .orElseThrow(() -> new ConverseException("No messageInfo to share for message id: "+messageId));

        Map<String, Set<UserDetails>> deliveryReceiptsByTime = userChatService.convertMapIdsToMapUserDetails(message.getDeliveryReceiptsByTime());
        Map<String, Set<UserDetails>> readReceiptsByTime = userChatService.convertMapIdsToMapUserDetails(message.getReadReceiptsByTime());

        return MessageInfoDto.builder()
                .deliveryReceiptsByTime(deliveryReceiptsByTime)
                .readReceiptsByTime(readReceiptsByTime)
                .build();
    }
}
