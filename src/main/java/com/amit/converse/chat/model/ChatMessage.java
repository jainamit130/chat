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
    private Map<String, Set<String>> deliveryReceiptsByTime = new TreeMap<>();
    private Map<String, Set<String>> readReceiptsByTime = new TreeMap<>();

    public void setDeliveryReceiptsByTime(Set<String> onlineUserIds) {
        if(onlineUserIds.size()==0){
            return;
        }
        String now = Instant.now().toString();
        deliveryReceiptsByTime.put(now, onlineUserIds);
    }

    public void setReadReceiptsByTime(Set<String> onlineAndActiveUserIds) {
        if(onlineAndActiveUserIds.size()==0){
            return;
        }
        String now = Instant.now().toString();
        readReceiptsByTime.put(now, onlineAndActiveUserIds);
    }

    public void addUserToReadReceipt(String timestamp,String userId){
        Set<String> userIds = readReceiptsByTime.getOrDefault(timestamp,new HashSet());
        userIds.add(userId);
        readReceiptsByTime.put(timestamp,userIds);
        return;
    }

    public void addUserToDeliveredReceipt(String timestamp,String userId){
        Set<String> userIds = readReceiptsByTime.getOrDefault(timestamp,new HashSet());
        userIds.add(userId);
        deliveryReceiptsByTime.put(timestamp,userIds);
        return;
    }
}
