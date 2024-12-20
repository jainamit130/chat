package com.amit.converse.chat.model;

import com.amit.converse.chat.dto.OnlineStatusDto;
import com.amit.converse.chat.service.WebSocketMessageService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
@Document(collection = "user")
public class User {

    @Id
    private String id;
    private String userId;
    private Set<String> chatRoomIds = new HashSet<>();
    private String username;
    private String password;
    private String status;
    private Instant lastSeenTimestamp;
    private Instant creationDate;
    private final WebSocketMessageService webSocketService;
    public void addChatRoom(String chatRoomId){
        chatRoomIds.add(chatRoomId);
    }

    public void removeChatRoom(String chatRoomId) {
        if (chatRoomIds.contains(chatRoomId)) {
            chatRoomIds.remove(chatRoomId);
        }
    }

    public void notifyStatus(ConnectionStatus status) {
        OnlineStatusDto onlineStatusDto = OnlineStatusDto.builder().status(status).username(username).build();
        for(String chatRoomId: chatRoomIds){
            webSocketService.sendOnlineStatusToGroup(chatRoomId,onlineStatusDto);
        }
        return;
    }
}