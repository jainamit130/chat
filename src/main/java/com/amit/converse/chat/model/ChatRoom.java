package com.amit.converse.chat.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@Document(collection = "chatRooms")
public class ChatRoom {

    @Id
    private String id;
    private String name;
    private List<String> userIds;
    @NotBlank
    private ChatRoomType chatRoomType;
    private String createdBy;
    private Instant createdAt;
    private String creatorUsername;
    private String recipientUsername;
    private transient ChatMessage latestMessage;
    private transient Integer unreadMessageCount;
    private transient Boolean exitedMember;
    private Integer totalMessagesCount;
    private Map<String, Integer> readMessageCounts;
    private Map<String, Integer> deliveredMessageCounts;
    // Clear Chat feature
    private Map<String, Instant> userFetchStartTimeMap;
    // Exit Group Feature
    private Map<String,Instant> exitedMembers;

    public void incrementTotalMessagesCount() {
        if(totalMessagesCount==null){
            totalMessagesCount=0;
        }
        totalMessagesCount++;
    }

    public void allMessagesMarkedDelivered(String userId) {
        deliveredMessageCounts.put(userId, totalMessagesCount);
    }

    public void allMessagesMarkedRead(String userId) {
        readMessageCounts.put(userId, totalMessagesCount);
    }

    public int getUnreadMessageCount(String userId) {
        return totalMessagesCount-readMessageCounts.getOrDefault(userId, 0);
    }

    public int  getUndeliveredMessageCount(String userId) {
        return totalMessagesCount-deliveredMessageCounts.getOrDefault(userId, 0);
    }

    public Map<String,Instant> getUserFetchStartTimeMap() {
        if(userFetchStartTimeMap==null){
            return new HashMap<>();
        }
        return userFetchStartTimeMap;
    }

    public void exitGroup(String userId){
        exitedMembers.put(userId,Instant.now());
    }

    public boolean isExitedMember(String userId) {
        if(exitedMembers.containsKey(userId)){
            exitedMember=true;
            return true;
        }
        exitedMember=false;
        return false;
    }
}
