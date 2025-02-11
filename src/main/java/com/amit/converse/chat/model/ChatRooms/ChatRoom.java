package com.amit.converse.chat.model.ChatRooms;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.Enums.ChatRoomType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.*;

@Data
@SuperBuilder
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DirectChat.class, name = "DIRECT"),
        @JsonSubTypes.Type(value = GroupChat.class, name = "GROUP"),
        @JsonSubTypes.Type(value = SelfChat.class, name = "SELF")
})
@Document(collection = "chatRooms")
public abstract class ChatRoom implements IChatRoom {

    @PersistenceCreator
    public ChatRoom(String id, List<String> userIds, ChatRoomType chatRoomType, Instant createdAt, ChatMessage latestMessage, Map<String, Instant> userFetchStartTimeMap, Set<String> deletedForUsers, Integer totalMessageCount, Map<String, Integer> readMessageCount, Map<String, Instant> lastVisitedTimestamp) {
        this.id = id;
        this.userIds = userIds;
        this.chatRoomType = chatRoomType;
        this.createdAt = createdAt;
        this.latestMessage = latestMessage;
        this.userFetchStartTimeMap = userFetchStartTimeMap;
        this.deletedForUsers = deletedForUsers;
        this.totalMessageCount = totalMessageCount;
        this.readMessageCount = readMessageCount;
        this.lastVisitedTimestamp = lastVisitedTimestamp;
    }

    public ChatRoom(ChatRoomType chatRoomType) {
        this.chatRoomType = chatRoomType;
    }

    @Id
    protected String id;
    protected List<String> userIds;
    protected final ChatRoomType chatRoomType;
    protected Instant createdAt;
    protected transient ChatMessage latestMessage;

    // Clear Chat Feature
    @Builder.Default
    private Map<String, Instant> userFetchStartTimeMap = new HashMap<>();

    @Builder.Default
    private Set<String> deletedForUsers = new HashSet<>();

    @Builder.Default
    private Integer totalMessageCount = 0;

    @Builder.Default
    private Map<String, Integer> readMessageCount = new HashMap<>();

    @Builder.Default
    private Map<String, Instant> lastVisitedTimestamp = new HashMap<>();

    public void setUserIds(List<String> userIds) {
        Set<String> userIdsSet = Set.copyOf(userIds);
        this.userIds = new ArrayList<>(userIdsSet);
    }

    public Integer getDeletedForUsersCount() {
        return getDeletedForUsers().size();
    }

    public abstract Boolean isDeletable();

    public Integer getMemberCount() {
        return userIds.size();
    }

    public Integer getTotalMemberCount() {
        return getMemberCount();
    }

    @Override
    public Instant getUserFetchStartTime(String userId) { return userFetchStartTimeMap.getOrDefault(userId,getCreatedAt()); }

    @Override
    public void deleteChat(String userId) {
        deletedForUsers.add(userId);
    }

    @Override
    public void clearChat(String userId) {
        Map<String,Instant> userFetchStartTimeMap = getUserFetchStartTimeMap();
        userFetchStartTimeMap.put(userId,Instant.now());
        setUserFetchStartTimeMap(userFetchStartTimeMap);
    }

    private void updateLastVisitedTimestamp(String userId) {
        lastVisitedTimestamp.put(userId,Instant.now());
    }

    @Override
    public Instant getLastVisitedTimestamp(String userId) {
        return lastVisitedTimestamp.getOrDefault(userId,Instant.now());
    }

    private Integer getReadMessageCount(String userId) {
        return readMessageCount.getOrDefault(userId,0);
    }

    @Override
    public Integer getUnreadMessageCount(String userId) {
        return getTotalMessageCount()-getReadMessageCount(userId);
    }

    public void readMessages(String userId) {
        updateLastVisitedTimestamp(userId);
        readMessageCount.put(userId,totalMessageCount);
    }

}
