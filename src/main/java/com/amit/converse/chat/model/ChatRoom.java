package com.amit.converse.chat.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private String createdAt;
    private transient ChatMessage latestMessage;
    private Map<String, Integer> unreadMessageCounts = new HashMap<>();

    public void incrementUnreadMessageCount(String userId) {
        unreadMessageCounts.put(userId, unreadMessageCounts.getOrDefault(userId, 0) + 1);
    }

    public void resetUnreadMessageCount(String userId) {
        unreadMessageCounts.put(userId, 0);
    }

    public int getUnreadMessageCount(String userId) {
        return unreadMessageCounts.getOrDefault(userId, 0);
    }
}
