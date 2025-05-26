package com.amit.converse.chat.model;

import com.amit.converse.chat.Redis.RedisSessionITransitionService;
import com.amit.converse.chat.State.State;
import com.amit.converse.chat.model.Enums.ConnectionStatus;
import com.amit.converse.chat.service.MessageProcessor.IDeliverableEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Document(collection = "user")
public class User implements IDeliverableEntity, UserDetails {

    @Id
    private String id;
    private String userId;

    @Builder.Default
    private Set<String> chatRoomIds = new HashSet<>();

    // Exited ChatRoom Ids with Integer representing unread messages
    @Builder.Default
    private Map<String,Integer> exitedChatRoomIds = new HashMap<>();

    @Builder.Default
    private List<String> deletedChatRoomIds = new ArrayList<>();

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

    public List<String> getAllChatRoomIds() {
        List<String> allChatRoomIds = new ArrayList<>(chatRoomIds);
        allChatRoomIds.addAll(deletedChatRoomIds);
        return allChatRoomIds;
    }

    public String getStatus() {
        return status;
    }

    public Instant getLastSeenTimestamp() {
        if(lastSeenTimestamp==null) lastSeenTimestamp = creationDate;
        return lastSeenTimestamp;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setState(State state) {
        this.state = state;
    }

    public ConnectionStatus getConnectionStatus() {
        return getState().getConnectionStatus();
    }

    public void deleteChat(String chatRoomId) {
        chatRoomIds.remove(chatRoomId);
        deletedChatRoomIds.add(chatRoomId);
    }

    public void disconnectChat(String chatRoomId, Integer unreadMessageCount) {
        chatRoomIds.remove(chatRoomId);
        exitedChatRoomIds.put(chatRoomId,unreadMessageCount);
    }

    public void connectChat(String chatRoomId) {
        exitedChatRoomIds.remove(chatRoomId);
        deletedChatRoomIds.remove(chatRoomId);
        chatRoomIds.add(chatRoomId);
    }

    public boolean isExited(String chatRoomId) { return exitedChatRoomIds.containsKey(chatRoomId); }

    public void updateLastSeenToNow() {
        lastSeenTimestamp = Instant.now();
    }

    public void transit() {
        state.transit();
    }

    public void transitSession() {
        redisSessionTransition.transit();
    }

    public boolean isOffline() {
        return getConnectionStatus().equals(ConnectionStatus.INACTIVE);
    }

    public String getDisplayName() {
        return username;
    }

    // User Details implementation methods

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return getUserId();
    }

    public String getUserId() {
        return userId;
    }

}