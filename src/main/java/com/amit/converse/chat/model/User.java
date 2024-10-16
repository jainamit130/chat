package com.amit.converse.chat.model;

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
@NoArgsConstructor
@Builder
@Document(collection = "user")
public class User {

    @Id
    private String id;
    private String userId;
    private Set<String> chatRoomIds = new HashSet<>();
    private String username;
    private String password;
    private OnlineStatus status;
    private Instant lastSeenTimestamp;
    private Instant creationDate;

    public void addChatRoom(String chatRoomId){
        chatRoomIds.add(chatRoomId);
    }

    public void removeChatRoom(String chatRoomId) {
        if (chatRoomIds.contains(chatRoomId)) {
            chatRoomIds.remove(chatRoomId);
        }
    }
}