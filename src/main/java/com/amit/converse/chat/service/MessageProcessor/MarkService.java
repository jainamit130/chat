package com.amit.converse.chat.service.MessageProcessor;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.MessageService.ChatMessageService;
import com.amit.converse.chat.service.Redis.RedisReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public abstract class MarkService {

    @Autowired
    protected RedisReadService redisReadService;

    @Autowired
    private ChatMessageService chatMessageService;

    private Map<String,List<String>> senderSpecificMessageIds;
    private List<ChatMessage> markedMessages;
    private List<String> onlineUserIds;

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
        }
    }

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
        List<ChatMessage> toBeDeliveredChatRoomMessages = chatMessageService.getMessagesOfChatFrom(chatRoom.getId(), user.getUserId(),lastDeliveredTimestamp);
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
        sendMessageMarkedNotificationToSender(chatRoom, message.getSenderId(),Collections.singletonList(message.getId()));
    }

    protected void saveAllMarkedMessages() {
        chatMessageService.saveMessages(markedMessages);
    }

}
