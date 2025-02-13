package com.amit.converse.chat.model.ChatRooms;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.Enums.ChatRoomType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.*;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DirectChat.class, name = "DIRECT"),
        @JsonSubTypes.Type(value = GroupChat.class, name = "GROUP"),
        @JsonSubTypes.Type(value = SelfChat.class, name = "SELF")
})
@Document(collection = "chatRooms")
public abstract class ChatRoom implements IChatRoom {

    public ChatRoom(ChatRoomType chatRoomType, List<String> userIds) {
        this.chatRoomType = chatRoomType;
        this.userIds = userIds;
        this.createdAt = Instant.now();
        this.lastVisitedTimestamp = new HashMap<>();
        this.deletedForUsers = new HashSet<>();
        this.userFetchStartTimeMap = new HashMap<>();
        this.totalMessageCount = 0;
        this.readMessageCount = new HashMap<>();
    }

    @Id
    protected String id;
    protected List<String> userIds;
    protected ChatRoomType chatRoomType;
    protected Instant createdAt;
    // Clear Chat Feature
    protected Map<String, Instant> userFetchStartTimeMap;
    protected Set<String> deletedForUsers;
    protected Integer totalMessageCount;
    protected Map<String, Integer> readMessageCount;
    protected Map<String, Instant> lastVisitedTimestamp;
    protected transient String chatRoomName;
    protected transient Integer unreadMessageCount;
    protected transient ChatMessage latestMessage;

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

    @Override
    public void readMessages(String userId) {
        updateLastVisitedTimestamp(userId);
        readMessageCount.put(userId,totalMessageCount);
    }

}
