package com.amit.converse.chat.service;

import com.amit.converse.chat.dto.GroupStatusDto;
import com.amit.converse.chat.dto.MessageStatusDto;
import com.amit.converse.chat.dto.MessageUpdateResponse;
import com.amit.converse.chat.dto.OnlineStatusDto;
import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.model.ChatRoom;
import com.amit.converse.chat.model.MessageType;
import com.amit.converse.chat.model.StatusType;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class WebSocketMessageService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendTypingStatusToGroup(Set<String> typingUsers, String chatRoomId) {
        messagingTemplate.convertAndSend("/topic/chat/" + chatRoomId, typingUsers);
    }

    public void sendNewGroupStatusToMember(String userId, ChatRoom savedChatRoom) {
        messagingTemplate.convertAndSend("/topic/user/" + userId, savedChatRoom);
    }

    public void sendMessage(String chatRoomId, ChatMessage chatMessage) {
        GroupStatusDto groupStatusDto = GroupStatusDto.builder().message(chatMessage).type(StatusType.MESSAGE).build();
        messagingTemplate.convertAndSend("/topic/chat/" + chatRoomId, groupStatusDto);
    }

    public void sendDeletedMessageStatus(String chatRoomId, String messageId) {
        GroupStatusDto groupStatusDto = GroupStatusDto.builder().id(messageId).type(StatusType.DELETE).build();
        messagingTemplate.convertAndSend("/topic/chat/" + chatRoomId, groupStatusDto);
    }

    public void sendExitMemberStatus(String chatRoomId, String userId) {
        GroupStatusDto groupStatusDto = GroupStatusDto.builder().id(userId).type(StatusType.EXIT).build();
        messagingTemplate.convertAndSend("/topic/chat/"+chatRoomId, groupStatusDto);
    }

    public void sendJoinedMemberStatus(String chatRoomId, String userId) {
        GroupStatusDto groupStatusDto = GroupStatusDto.builder().id(userId).type(StatusType.JOIN).build();
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