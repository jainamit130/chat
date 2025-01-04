package com.amit.converse.chat.service;

import com.amit.converse.chat.dto.GroupStatusDto;
import com.amit.converse.chat.dto.MessageStatusDto;
import com.amit.converse.chat.dto.Notification.ChatTransactionNotification;
import com.amit.converse.chat.dto.Notification.TypingNotification;
import com.amit.converse.chat.dto.OnlineStatusDto;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.ChatRoom;
import com.amit.converse.chat.model.Enums.NotificationType;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class WebSocketMessageServicee {

    private static final String address = "/topic";
    private final SimpMessagingTemplate messagingTemplate;

    public void sendChatTransactionNotification(ChatTransactionNotification notification, String chatRoomId) {
        messagingTemplate.convertAndSend("/topic/chat/"+chatRoomId,notification);
    }

    private void sendTypingUserNotification(TypingNotification notification,String chatRoomId) {

    }
    // send chatRoom to A => userId, chatRoom

    // A added B => chatRoomId, List<String>
    // A removed B => chatRoomId, List<String>
    // A exited => chatRoomId, List<String>
    // A is typing => chatRoomId, List<Usernames>
    // A sends message => chatRoomId, chatMessage
    // A deletes a message => chatRoomId, List<String>
    // Send message marked status => chatRoomId, senderOfMsg, List<String>
    public void sendJoinNotification(List<String> joinNotifications,String chatRoomId) {
        messagingTemplate.convertAndSend("/topic/chat/" + chatRoomId, joinNotifications);
    }

    public void sendTypingStatusToGroup(List<String> typingUsers, String chatRoomId) {
        messagingTemplate.convertAndSend("/topic/typing/" + chatRoomId, typingUsers);
    }

    public void sendNewGroupStatus(List<String> userId, ChatRoom savedChatRoom) {
        messagingTemplate.convertAndSend("/topic/user/" + userId, savedChatRoom);
    }

    public void sendMessage(String chatRoomId, ChatMessage chatMessage) {
        GroupStatusDto groupStatusDto = GroupStatusDto.builder().message(chatMessage).type(NotificationType.MESSAGE).build();
        messagingTemplate.convertAndSend("/topic/chat/" + chatRoomId, groupStatusDto);
    }

    public void sendDeletedMessageStatus(String chatRoomId, String messageId) {
        GroupStatusDto groupStatusDto = GroupStatusDto.builder().id(messageId).type(NotificationType.DELETE).build();
        messagingTemplate.convertAndSend("/topic/chat/" + chatRoomId, groupStatusDto);
    }

    public void sendExitMemberStatus(String chatRoomId, String userId, String username, String moderatorName) {
        GroupStatusDto groupStatusDto = GroupStatusDto.builder().id(userId).username(username).moderatorName(moderatorName).type(NotificationType.EXIT).build();
        messagingTemplate.convertAndSend("/topic/chat/"+chatRoomId, groupStatusDto);
    }

    public void sendJoinedMemberStatus(String chatRoomId, String userId, String username, String moderatorName) {
        GroupStatusDto groupStatusDto = GroupStatusDto.builder().id(userId).username(username).moderatorName(moderatorName).type(NotificationType.JOIN).build();
        messagingTemplate.convertAndSend("/topic/chat/"+chatRoomId, groupStatusDto);
    }

    public void sendMarkedMessageStatus(String chatRoomId, String senderId, List<String> messageIds, Boolean isDelivered){
        MessageStatusDto messageStatusDto = MessageStatusDto.builder().messageIds(messageIds).chatRoomId(chatRoomId).isDelivered(isDelivered).build();
        messagingTemplate.convertAndSend("/topic/message/"+senderId,messageStatusDto);
    }

    public void sendOnlineStatusToGroup(String chatRoomId, OnlineStatusDto onlineStatusDto) {
        messagingTemplate.convertAndSend("/topic/online/"+chatRoomId,onlineStatusDto);
    }
}