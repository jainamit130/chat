package com.amit.converse.chat.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.*;

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
    private transient Boolean isExited;
    private Set<String> deletedForUsers;
    private Integer totalMessagesCount;
    private Map<String, Integer> readMessageCounts;
    private Map<String, Integer> deliveredMessageCounts;
    // Clear Chat feature
    private Map<String, Instant> userFetchStartTimeMap;
    // Exit Group Feature Only For Groups
    private Map<String,Instant> exitedMembers;

    public void incrementTotalMessagesCount() {
        if(totalMessagesCount==null){
            totalMessagesCount=0;
        }
        totalMessagesCount++;
    }

    public void allMessagesMarkedRead(String userId) {
        readMessageCounts.put(userId, totalMessagesCount);
    }

    public int getReadMessageCount(String userId) {
        return readMessageCounts.getOrDefault(userId, 0);
    }

    public int getUnreadMessageCount(String userId) {
        if(isExitedMember(userId)){
            return 0;
        }
        return totalMessagesCount-getReadMessageCount(userId);
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
        if(exitedMembers==null){
            exitedMembers=new HashMap<>();
        }
        exitedMembers.put(userId,Instant.now());
    }

    public boolean isExitedMember(String userId) {
        if(exitedMembers==null){
            exitedMembers=new HashMap<>();
        }
        if(exitedMembers.containsKey(userId)){
            return true;
        }
        return false;
    }

    public void allMessagesMarkedDelivered(String userId) {
        deliveredMessageCounts.put(userId, totalMessagesCount);
    }

    public Set<String> getDeletedForUsers() {
        if(deletedForUsers==null){
            deletedForUsers = new HashSet<>();
        }
        return deletedForUsers;
    }

    public Map<String,Instant> getExitedMembers() {
        if(exitedMembers==null){
            exitedMembers = new HashMap<>();
        }
        return exitedMembers;
    }

    public Boolean deleteChat(String userId) {
        if(deletedForUsers==null){
            deletedForUsers=new HashSet<>();
        }
        deletedForUsers.add(userId);
        return true;
    }

    public void undoDeleteChat(String userId) {
        if(chatRoomType==ChatRoomType.INDIVIDUAL) {
            if(deletedForUsers==null){
                deletedForUsers=new HashSet<>();
            } else {
                deletedForUsers.remove(userId);
            }
        }
    }
}
