package com.amit.converse.chat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "chatMessages")
public class ChatMessage {

    @Id
    private String id;
    private String senderId;
    private String chatRoomId;
    private Instant timestamp;
    private String content;
    private boolean isEncrypted;
    private User user;
    private Map<Instant, Set<String>> deliveryReceiptsByTime = new TreeMap<>();
    private Map<Instant, Set<String>> readReceiptsByTime = new TreeMap<>();

    public void setDeliveryReceiptsByTime(Set<String> onlineUserIds) {
        Instant now = Instant.now();
        deliveryReceiptsByTime.put(now, onlineUserIds);
    }

    public void setReadReceiptsByTime(Set<String> onlineAndActiveUserIds) {
        Instant now = Instant.now();
        deliveryReceiptsByTime.put(now, onlineAndActiveUserIds);
    }

    public void addUserToReadReceipt(Instant timestamp,String userId){
        Set<String> userIds = readReceiptsByTime.getOrDefault(timestamp,new HashSet());
        userIds.add(userId);
        readReceiptsByTime.put(timestamp,userIds);
        return;
    }

    public void addUserToDeliveredReceipt(Instant timestamp,String userId){
        Set<String> userIds = readReceiptsByTime.getOrDefault(timestamp,new HashSet());
        userIds.add(userId);
        deliveryReceiptsByTime.put(timestamp,userIds);
        return;
    }
}
