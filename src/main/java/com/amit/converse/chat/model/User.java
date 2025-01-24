package com.amit.converse.chat.model;

import com.amit.converse.chat.Redis.RedisSessionITransitionService;
import com.amit.converse.chat.State.State;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@Builder
@Document(collection = "user")
public class User {

    @Id
    private String id;
    private String userId;

    @Builder.Default
    private Set<String> chatRoomIds = new HashSet<>();

    @Builder.Default
    private Set<String> adminRoleChatRoomIds = new HashSet<>();

    private String status;
    private String username;
    private String password;
    private State state;
    private RedisSessionITransitionService redisSessionTransition;
    private Instant lastSeenTimestamp;
    private Instant creationDate;

    public void disconnectChat(String chatRoomId) {
        chatRoomIds.remove(chatRoomId);
    }

    public void connectChat(String chatRoomId) {
        chatRoomIds.add(chatRoomId);
    }

    public void addChatRoom(String chatRoomId){
        chatRoomIds.add(chatRoomId);
    }

    public void updateLastSeenToNow() {
        lastSeenTimestamp = Instant.now();
    }

    public void transit() {
        redisSessionTransition.transit();
    }
}