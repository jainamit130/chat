package com.amit.converse.chat.service;

import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.model.ChatRoom;
import com.amit.converse.chat.model.MessageStatus;
import com.amit.converse.chat.model.User;
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

    public ChatMessage addMessage(String chatRoomId, ChatMessage message) {
        ChatRoom chatRoom = groupService.getChatRoom(chatRoomId);
        User user = userService.getUser(message.getSenderId());

        message.setTimestamp(Instant.now());
        message.setChatRoomId(chatRoom.getId());
        message.setUser(user);
        message.setMessageStatus(MessageStatus.PENDING);
        chatRoom.incrementTotalMessagesCount();
        message.setDeliveryReceiptsByTime(new HashSet<>());

        Set<String> onlineUserIds = redisService.filterOnlineUsers(chatRoom.getUserIds());
        Set<String> onlineAndActiveUserIds = new HashSet<>(Collections.singleton(message.getSenderId()));
        for (String userId : onlineUserIds) {
            markAllMessagesDelivered(userId);
            if (redisService.isUserInChatRoom(chatRoomId,userId)) {
                if(userId!=message.getSenderId()) {
                    onlineAndActiveUserIds.add(userId);
                }
                markAllMessagesRead(chatRoomId,userId);
            }
        }
        ChatMessage savedMessage = chatMessageRepository.save(message);
        groupService.saveChatRoom(chatRoom);
        return savedMessage;
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
        for (ChatMessage messageToBeMarked: messagesToBeMarked){
            if(isDelivered){
                messageToBeMarked.addUserToDeliveredReceipt(timestampStr,userId);
                if(messageToBeMarked.getDeliveryReceiptsByTime().size()==chatRoom.getUserIds().size()){
                    messageToBeMarked.setMessageStatus(MessageStatus.DELIVERED);
                    webSocketMessageService.sendMarkedMessageStatus(chatRoomId,messageToBeMarked.getSenderId(),false);
                }
            } else {
                messageToBeMarked.addUserToReadReceipt(timestampStr,userId);
                if(messageToBeMarked.getReadReceiptsByTime().size()==chatRoom.getUserIds().size()){
                    messageToBeMarked.setMessageStatus(MessageStatus.READ);
                    webSocketMessageService.sendMarkedMessageStatus(chatRoomId,messageToBeMarked.getSenderId(),false);
                }
            }
            chatMessageRepository.save(messageToBeMarked);
        }
        if(!isDelivered) {
            chatRoom.allMessagesMarkedRead(userId);
        } else {
            chatRoom.allMessagesMarkedDelivered(userId);
        }
        groupService.saveChatRoom(chatRoom);
        return;
    }
}
