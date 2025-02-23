package com.amit.converse.chat.model;

import com.amit.converse.chat.Redis.RedisSessionITransitionService;
import com.amit.converse.chat.State.Offline;
import com.amit.converse.chat.State.State;
import com.amit.converse.chat.service.MessageProcessor.IDeliverableEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Document(collection = "user")
public class User implements IDeliverableEntity {

    @Id
    private String id;
    private String userId;

    @Builder.Default
    private Set<String> chatRoomIds = new HashSet<>();

    // Exited ChatRoom Ids with Integer representing unread messages
    @Builder.Default
    private Map<String,Integer> exitedChatRoomIds = new HashMap<>();

    @Builder.Default
    private Set<String> adminRoleChatRoomIds = new HashSet<>();

    @Builder.Default
    private String status = "Hey there! I am using Converse";
    private String username;
    private String password;
    @Transient
    private State state;
    @Transient
    private RedisSessionITransitionService redisSessionTransition;
    private Instant lastSeenTimestamp;
    private Instant creationDate;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setState(State state) {
        this.state = state;
        if(state instanceof Offline) updateLastSeenTimestamp();
    }

    public void updateLastSeenTimestamp() {
        this.setLastSeenTimestamp(Instant.now());
    }

    public void disconnectChat(String chatRoomId, Integer unreadMessageCount) {
        chatRoomIds.remove(chatRoomId);
        exitedChatRoomIds.put(chatRoomId,unreadMessageCount);
    }

    public void connectChat(String chatRoomId) {
        exitedChatRoomIds.remove(chatRoomId);
        chatRoomIds.add(chatRoomId);
    }

    public boolean isExited(String chatRoomId) { return exitedChatRoomIds.containsKey(chatRoomId); }

    public void updateLastSeenToNow() {
        lastSeenTimestamp = Instant.now();
    }

    public void transit() {
        redisSessionTransition.transit();
    }
}