package com.amit.converse.chat.service;

import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.model.ChatRoom;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.model.UserMessageStats;
import com.amit.converse.chat.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final GroupService groupService;
    private final ChatMessageRepository chatMessageRepository;
    private final UserService userService;
    private final RedisService redisService;
    private final WebSocketMessageService webSocketMessageService;

    public void addMessage(String chatRoomId, ChatMessage message) {
        ChatRoom chatRoom = groupService.getChatRoom(chatRoomId);
        User user = userService.getUser(message.getSenderId());

        message.setTimestamp(Instant.now());
        message.setChatRoomId(chatRoom.getId());
        message.setUser(user);

        chatRoom.incrementTotalMessagesCount();

        Set<String> onlineUserIds = redisService.filterOnlineUsers(chatRoom.getUserIds());
        message.setDeliveryReceiptsByTime(onlineUserIds);
        Set<String> onlineAndActiveUserIds = new HashSet<>(Collections.singleton(message.getSenderId()));
        UserMessageStats userMessageStats = chatRoom.getUserStats(user.getUserId());
        userMessageStats.incrementSentMessages();
        userMessageStats.incrementDeliveredMessages();
        userMessageStats.incrementReadMessages();
        for (String userId : onlineUserIds) {
            userMessageStats.incrementDeliveredMessages();
            chatRoom.allMessagesMarkedDelivered(userId);
            if (redisService.isUserInChatRoom(chatRoomId,userId)) {
                if(userId!=message.getSenderId()) {
                    onlineAndActiveUserIds.add(userId);
                    userMessageStats.incrementReadMessages();
                }
                chatRoom.allMessagesMarkedRead(userId);
            }
        }
        message.setReadReceiptsByTime(onlineAndActiveUserIds);
        chatMessageRepository.save(message);
        chatRoom.updateUserStats(user.getUserId(),userMessageStats);
        groupService.saveChatRoom(chatRoom);
    }

    public void markAllMessagesDelivered(String userId){
        User user = userService.getUser(userId);
        if (user != null) {
            Set<String> chatRoomIds = user.getChatRoomIds();
            if (chatRoomIds != null && !chatRoomIds.isEmpty()) {
                for(String chatRoomId: chatRoomIds){
                    ChatRoom chatRoom = groupService.getChatRoom(chatRoomId);
                    Integer toBeDeliveredMessagesCount=chatRoom.getUndeliveredMessageCount(userId);
                    if(toBeDeliveredMessagesCount>0)
                        markAllMessages(chatRoomId, userId, true,toBeDeliveredMessagesCount);
                }
            }
        } else {
            System.out.println("User not found: " + userId);
        }
    }


    public void markAllMessagesRead(String chatRoomId,String userId){
        ChatRoom chatRoom = groupService.getChatRoom(chatRoomId);
        Integer toBeMarkedMessagesCount=chatRoom.getUnreadMessageCount(userId);
        if(toBeMarkedMessagesCount>0)
            markAllMessages(chatRoomId,userId,false,toBeMarkedMessagesCount);
    }

    public void markAllMessages(String chatRoomId,String userId, Boolean isDelivered, Integer toBeMarkedMessagesCount) {
        ChatRoom chatRoom = groupService.getChatRoom(chatRoomId);
        PageRequest pageRequest = PageRequest.of(0, toBeMarkedMessagesCount, Sort.by(Sort.Direction.DESC, "timestamp"));
        List<ChatMessage> messagesToBeMarked = groupService.getMessagesToBeMarked(chatRoomId,pageRequest);
        String timestampStr = Instant.now().toString();
        for (ChatMessage unreadMessage: messagesToBeMarked){
            UserMessageStats userMessageStats = chatRoom.getUserStats(unreadMessage.getSenderId());
            if(isDelivered){
                unreadMessage.addUserToDeliveredReceipt(timestampStr,userId);
                userMessageStats.incrementDeliveredMessages();
            } else {
                unreadMessage.addUserToReadReceipt(timestampStr,userId);
                userMessageStats.incrementReadMessages();
            }
            chatRoom.updateUserStats(unreadMessage.getSenderId(),userMessageStats);
            chatMessageRepository.save(unreadMessage);
        }
        if(!isDelivered) {
            chatRoom.allMessagesMarkedRead(userId);
            webSocketMessageService.sendMarkedMessageStatus(chatRoomId,userId,timestampStr,false);
        } else {
            chatRoom.allMessagesMarkedDelivered(userId);
            webSocketMessageService.sendMarkedMessageStatus(chatRoomId,userId,timestampStr,true);
        }
        groupService.saveChatRoom(chatRoom);
        return;
    }
}
