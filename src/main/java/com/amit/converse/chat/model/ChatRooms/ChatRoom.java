package com.amit.converse.chat.model.ChatRooms;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.Enums.ChatRoomType;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.*;

@Data
@Builder
@AllArgsConstructor
@Document(collection = "chatRooms")
public abstract class ChatRoom implements IChatRoom {
    @Id
    protected String id;
    protected List<String> userIds;
    protected Instant createdAt;
    protected transient ChatMessage latestMessage;
    protected transient Integer unreadMessageCount;
    @NotBlank
    private ChatRoomType chatRoomType;

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
    private Map<String, Integer> deliveredMessageCount = new HashMap<>();

    public Boolean deleteChat(String userId) {
        if(deletedForUsers==null){
            deletedForUsers=new HashSet<>();
        }
        deletedForUsers.add(userId);
        return true;
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

    public void clearChat(String userId) {
        Map<String,Instant> userFetchStartTimeMap = getUserFetchStartTimeMap();
        userFetchStartTimeMap.put(userId,Instant.now());
        setUserFetchStartTimeMap(userFetchStartTimeMap);
    }

    public void deliverMessages(String userId) {
        deliveredMessageCount.put(userId,totalMessageCount);
    }

    public void readMessages(String userId) {
        readMessageCount.put(userId,totalMessageCount);
    }

}
