package com.amit.converse.chat.service.MessageProcessor;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.Messages.Message;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.MessageService.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MarkDeliveredService {

    @Autowired
    private ChatMessageService chatMessageService;

    // Returns a Map<String,List<String>> chatRoomIdToMessageIdsMap required for sending out marked notification to sender
    public Map<String,List<String>> markMessages(List<Message> messages, User user) {
        String currentTimestampStr = Instant.now().toString();
        Map<String,List<String>> chatRoomIdToMessageIdsMap = new HashMap<>();
        messages.stream().forEach(message -> {
            message.deliverMessage(currentTimestampStr, user.getUserId());
            List<String> messageIds = chatRoomIdToMessageIdsMap.getOrDefault(message.getChatRoomId(),new ArrayList<>());
            messageIds.add(message.getId());
            chatRoomIdToMessageIdsMap.put(message.getChatRoomId(),messageIds);
        });
        chatMessageService.saveMessages(messages);
        return chatRoomIdToMessageIdsMap;
    }

    public void deliver(List<IChatRoom> chatRooms, User user) {
        List<Message> toBeDeliveredMessages = new ArrayList<>();
        for(IChatRoom chatRoom: chatRooms) {
            Instant lastDeliveredTimestamp = user.getLastSeenTimestamp();
            toBeDeliveredMessages.addAll(chatMessageService.getMessagesOfChatFrom(chatRoom.getId(), user.getUserId(),lastDeliveredTimestamp));
        }
        Map<String,List<String>> chatRoomIdToMessageIdsMap = markMessages(toBeDeliveredMessages,user);
        chatMessageService.sendMessageMarkedNotification();
    }

    public void deliver(Message message) {

    }
}
