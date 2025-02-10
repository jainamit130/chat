package com.amit.converse.chat.model.ChatRooms;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.Enums.ChatRoomType;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.*;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "chatRooms")
public abstract class ChatRoom implements IChatRoom {
    @Id
    protected String id;
    protected List<String> userIds;
    protected Instant createdAt;
    protected transient ChatMessage latestMessage;
    @NotBlank
    protected ChatRoomType chatRoomType;

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
