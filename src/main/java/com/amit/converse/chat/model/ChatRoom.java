package com.amit.converse.chat.model;

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
    private String createdBy;
    private Instant createdAt;
    private transient ChatMessage latestMessage;
    private transient Integer unreadMessageCount;
    private Integer totalMessagesCount;
    private Map<String, Integer> readMessageCounts;
    private Map<String, Integer> deliveredMessageCounts;

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
}
