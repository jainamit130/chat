package com.amit.converse.chat.service.MessageProcessor;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.MessageService.ChatMessageService;
import com.amit.converse.chat.service.Redis.RedisReadService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public abstract class MarkService {

    protected RedisReadService redisReadService;
    protected ChatMessageService chatMessageService;
    private Map<String,List<String>> senderSpecificMessageIds;
    private List<ChatMessage> markedMessages;
    private List<String> onlineUserIds;

    public MarkService(RedisReadService redisReadService, ChatMessageService chatMessageService) {
        this.redisReadService = redisReadService;
        this.chatMessageService = chatMessageService;
        this.senderSpecificMessageIds = new HashMap<>();
        this.markedMessages = new ArrayList<>();
        this.onlineUserIds = new ArrayList<>();
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

    private void collectToNotifyMessageToSender(ChatMessage message) {
        List<String> senderMessageIds = senderSpecificMessageIds.getOrDefault(message.getSenderId(),new ArrayList<>());
        senderMessageIds.add(message.getId());
        senderSpecificMessageIds.put(message.getSenderId(),senderMessageIds);
        processMessage(message);
    }

    private void markMessages(List<ChatMessage> messages, Instant markTimestamp, String userId, Integer memberCount) {
        messages.stream().forEach(message -> {
            Integer markedUsersCount = markMessage(message,markTimestamp, userId);
            if(memberCount.equals(markedUsersCount)) collectToNotifyMessageToSender(message);
        });
        return;
    }

    protected List<String> getOnlineUserIds() {
        return Collections.unmodifiableList(onlineUserIds);
    }

    protected void setOnlineUserIds(List<String> onlineUserIds) {
        this.onlineUserIds = onlineUserIds;
    }

    protected void mark(IChatRoom chatRoom, User user) {
        Instant lastVisitedTimestamp = getLastVisitedTimestamp(chatRoom,user);
        List<ChatMessage> toBeMarkedChatRoomMessages = chatMessageService.getMessagesToBeMarked(chatRoom,user,lastVisitedTimestamp);
        markMessages(toBeMarkedChatRoomMessages,Instant.now(),user.getUserId(),chatRoom.getMemberCount());
        markedMessages.addAll(toBeMarkedChatRoomMessages);
        sendSenderSpecificMessageMarkedNotification(chatRoom,senderSpecificMessageIds);
    }

    protected void mark(IChatRoom chatRoom,ChatMessage message) {
        // Online UserIds for MarkDeliveredService or Online and ChatActive UserIds for MarkReadService
        List<String> activeUserIds = getActiveUserIds(chatRoom);
        Instant markTimestamp = Instant.now();
        for(String onlineUserId:activeUserIds) {
            markMessages(Collections.singletonList(message),markTimestamp,onlineUserId,chatRoom.getMemberCount());
        }
        markedMessages.add(message);
        sendSenderSpecificMessageMarkedNotification(chatRoom,senderSpecificMessageIds);
    }

    protected void saveAllMarkedMessages() {
        chatMessageService.saveMessages(markedMessages);
    }

    public abstract Integer markMessage(ChatMessage message, Instant timestamp,String userId);
    public abstract List<String> getActiveUserIds(IChatRoom chatRoom);
    public abstract void processMessage(ChatMessage message);
    public abstract void sendMessageMarkedNotification(String chatRoomId, String senderId, List<String> messageIds);
    public abstract Instant getLastVisitedTimestamp(IChatRoom chatRoom,User user);

    public void clearMarkService() {
        this.senderSpecificMessageIds = new HashMap<>();
        this.markedMessages = new ArrayList<>();
        this.onlineUserIds = new ArrayList<>();
    }
}
