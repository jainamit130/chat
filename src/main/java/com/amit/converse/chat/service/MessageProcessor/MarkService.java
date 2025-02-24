package com.amit.converse.chat.service.MessageProcessor;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.MessageService.ChatMessageService;
import com.amit.converse.chat.service.Redis.RedisReadService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@Scope("prototype")
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

    protected List<String> getOnlineUserIds() {
        return Collections.unmodifiableList(onlineUserIds);
    }

    protected void setOnlineUserIds(List<String> onlineUserIds) {
        this.onlineUserIds = onlineUserIds;
    }

    public abstract Integer markMessage(ChatMessage message, String timestamp,String userId);

    public abstract List<String> getActiveUserIds(IChatRoom chatRoom);

    private void sendMessageMarkedNotificationToSender(IChatRoom chatRoom, String senderId, List<String> messageIds) {
        if(redisReadService.isUserOnline(senderId))
            chatMessageService.sendMessageMarkedNotification(chatRoom.getId(),senderId,messageIds);
    }

    private void sendSenderSpecificMessageMarkedNotification(IChatRoom chatRoom, Map<String, List<String>> senderSpecificMessageIds) {
        for (String senderId:senderSpecificMessageIds.keySet()) {
            sendMessageMarkedNotificationToSender(chatRoom,senderId,senderSpecificMessageIds.get(senderId));
        }
    }

    private void collectToNotifyMessageToSender(Integer markedUsersCount, ChatMessage message,Integer memberCount) {
        if(memberCount.equals(markedUsersCount)) {
            List<String> senderMessageIds = senderSpecificMessageIds.getOrDefault(message.getSenderId(),new ArrayList<>());
            senderMessageIds.add(message.getId());
            senderSpecificMessageIds.put(message.getSenderId(),senderMessageIds);
            processMessage(message);
        }
    }

    public abstract void processMessage(ChatMessage message);

    private void markMessages(List<ChatMessage> messages, String userId, Integer memberCount) {
        String currentTimestampStr = Instant.now().toString();
        messages.stream().forEach(message -> {
            Integer markedUsersCount = markMessage(message,currentTimestampStr, userId);
            collectToNotifyMessageToSender(markedUsersCount,message,memberCount);
        });
        return;
    }

    protected void mark(IChatRoom chatRoom, User user) {
        Instant lastDeliveredTimestamp = user.getLastSeenTimestamp();
        List<ChatMessage> toBeDeliveredChatRoomMessages = chatMessageService.getMessagesOfChatFrom(chatRoom, user,lastDeliveredTimestamp);
        markMessages(toBeDeliveredChatRoomMessages,user.getUserId(),chatRoom.getMemberCount());
        markedMessages.addAll(toBeDeliveredChatRoomMessages);
        sendSenderSpecificMessageMarkedNotification(chatRoom,senderSpecificMessageIds);
    }

    protected void mark(IChatRoom chatRoom,ChatMessage message) {
        // Online UserIds for MarkDeliveredService or Online and ChatActive UserIds for MarkReadService
        List<String> activeUserIds = getActiveUserIds(chatRoom);
        for(String onlineUserId:activeUserIds) {
            markMessages(Collections.singletonList(message),onlineUserId,chatRoom.getMemberCount());
        }
        markedMessages.add(message);
        sendMessageMarkedNotificationToSender(chatRoom, message.getSenderId(),Collections.singletonList(message.getId()));
    }

    protected void saveAllMarkedMessages() {
        chatMessageService.saveMessages(markedMessages);
    }

}
