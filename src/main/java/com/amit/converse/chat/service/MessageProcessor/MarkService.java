package com.amit.converse.chat.service.MessageProcessor;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.MessageService.ChatMessageService;
import com.amit.converse.chat.service.Redis.RedisReadService;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public abstract class MarkService {

    protected RedisReadService redisReadService;
    protected ChatMessageService chatMessageService;

    public MarkService(RedisReadService redisReadService, ChatMessageService chatMessageService) {
        this.redisReadService = redisReadService;
        this.chatMessageService = chatMessageService;
    }

    private void sendMessageMarkedNotificationToSender(IChatRoom chatRoom, String senderId, List<String> messageIds) {
        if(redisReadService.isUserOnline(User.builder().userId(senderId).build()))
            sendMessageMarkedNotification(chatRoom.getId(),senderId,messageIds);
    }

    private void sendSenderSpecificMessageMarkedNotification(IChatRoom chatRoom, Map<String, List<String>> senderSpecificMessageIds) {
        for (String senderId:senderSpecificMessageIds.keySet()) {
            sendMessageMarkedNotificationToSender(chatRoom,senderId,senderSpecificMessageIds.get(senderId));
        }
    }

    private void collectToNotifyMessageToSender(ChatMessage message, Map<String, List<String>> senderSpecificMessageIds) {
        List<String> senderMessageIds = senderSpecificMessageIds.getOrDefault(message.getSenderId(),new ArrayList<>());
        senderMessageIds.add(message.getId());
        senderSpecificMessageIds.put(message.getSenderId(),senderMessageIds);
        processMessage(message);
    }

    private void markMessages(ChatRoom chatRoom, List<ChatMessage> messages, Instant markTimestamp, String userId,
                              Integer memberCount, Map<String, List<String>> senderSpecificMessageIds) {
        for (ChatMessage message : messages) {
            Integer markedUsersCount = markMessage(chatRoom,message, markTimestamp, userId);
            if (memberCount.equals(markedUsersCount)) {
                collectToNotifyMessageToSender(message, senderSpecificMessageIds);
            }
        }
    }

    protected void mark(ChatRoom chatRoom, User user, MarkingContext context) {
        Instant lastVisitedTimestamp = getLastVisitedTimestamp(chatRoom,user);
        List<ChatMessage> toBeMarkedChatRoomMessages = chatMessageService.getMessagesOfChatRoom(chatRoom,user,lastVisitedTimestamp);
        markMessages(chatRoom,toBeMarkedChatRoomMessages,Instant.now(),user.getUserId(),chatRoom.getMemberCount(),context.getSenderSpecificMessageIds());
        context.getMarkedMessages().addAll(toBeMarkedChatRoomMessages);
        sendSenderSpecificMessageMarkedNotification(chatRoom,context.getSenderSpecificMessageIds());
    }

    protected void mark(ChatRoom chatRoom,ChatMessage message, MarkingContext context) {
        // Online UserIds for MarkDeliveredService or Online and ChatActive UserIds for MarkReadService
        List<String> activeUserIds = getActiveUserIds(chatRoom, context);
        Instant markTimestamp = Instant.now();
        for(String onlineUserId:activeUserIds) {
            markMessages(chatRoom,Collections.singletonList(message),markTimestamp,onlineUserId,chatRoom.getMemberCount(),context.getSenderSpecificMessageIds());
        }
        context.getMarkedMessages().add(message);
        sendSenderSpecificMessageMarkedNotification(chatRoom,context.getSenderSpecificMessageIds());
        context.getSenderSpecificMessageIds().clear();
    }

    protected void saveAllMarkedMessages(MarkingContext context) {
        chatMessageService.saveMessages(context.getMarkedMessages());
    }

    public abstract Integer markMessage(ChatRoom chatRoom, ChatMessage message, Instant timestamp, String userId);
    public abstract List<String> getActiveUserIds(IChatRoom chatRoom, MarkingContext context);
    public abstract void processMessage(ChatMessage message);
    public abstract void sendMessageMarkedNotification(String chatRoomId, String senderId, List<String> messageIds);
    public abstract Instant getLastVisitedTimestamp(IChatRoom chatRoom,User user);
}
