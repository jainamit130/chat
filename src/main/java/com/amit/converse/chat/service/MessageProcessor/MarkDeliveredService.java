package com.amit.converse.chat.service.MessageProcessor;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.Messages.Message;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.MessageService.ChatMessageService;
import com.amit.converse.chat.service.Redis.RedisReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class MarkDeliveredService {

    @Autowired
    private RedisReadService redisReadService;

    @Autowired
    private ChatMessageService chatMessageService;

    private Map<String,List<String>> senderSpecificMessageIds;
    private List<ChatMessage> deliveredMessages;

    private void collectToNotifyMessageToSender(Integer deliveredUsersCount, ChatMessage message,Integer memberCount) {
        if(memberCount.equals(deliveredUsersCount)) {
            List<String> senderMessageIds = senderSpecificMessageIds.getOrDefault(message.getSenderId(),new ArrayList<>());
            senderMessageIds.add(message.getId());
            senderSpecificMessageIds.put(message.getSenderId(),senderMessageIds);
        }
    }

    private void sendMessageMarkedNotificationToSender(IChatRoom chatRoom,String senderId,List<String> messageIds) {
        if(redisReadService.isUserOnline(senderId))
            chatMessageService.sendMessageMarkedNotification(chatRoom.getId(),senderId,messageIds);
    }

    private void sendSenderSpecificMessageMarkedNotification(IChatRoom chatRoom, Map<String, List<String>> senderSpecificMessageIds) {
        for (String senderId:senderSpecificMessageIds.keySet()) {
            sendMessageMarkedNotificationToSender(chatRoom,senderId,senderSpecificMessageIds.get(senderId));
        }
    }

    // Returns a Map<String,List<String>> chatRoomIdToMessageIdsMap required for sending out marked notification to sender
    private void markMessages(List<ChatMessage> messages, String userId, Integer memberCount) {
        String currentTimestampStr = Instant.now().toString();
        messages.stream().forEach(message -> {
            Integer deliveredUsersCount = message.deliverMessage(currentTimestampStr, userId);
            collectToNotifyMessageToSender(deliveredUsersCount,message,memberCount);
        });
        return;
    }

    public void saveAllDeliveredMessages() {
        chatMessageService.saveMessages(deliveredMessages);
    }

    public void deliver(IChatRoom chatRoom, User user) {
        Instant lastDeliveredTimestamp = user.getLastSeenTimestamp();
        List<ChatMessage> toBeDeliveredChatRoomMessages = chatMessageService.getMessagesOfChatFrom(chatRoom.getId(), user.getUserId(),lastDeliveredTimestamp);
        markMessages(toBeDeliveredChatRoomMessages,user.getUserId(),chatRoom.getMemberCount());
        deliveredMessages.addAll(toBeDeliveredChatRoomMessages);
        sendSenderSpecificMessageMarkedNotification(chatRoom,senderSpecificMessageIds);
    }

    public void deliver(IChatRoom chatRoom,ChatMessage message) {
        List<String> onlineUserIds = new ArrayList<>(redisReadService.filterOnlineUsers(chatRoom));
        for(String onlineUserId:onlineUserIds) {
            markMessages(Collections.singletonList(message),onlineUserId,chatRoom.getMemberCount());
        }
        sendMessageMarkedNotificationToSender(chatRoom, message.getSenderId(),Collections.singletonList(message.getId()));
    }
}
