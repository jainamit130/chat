package com.amit.converse.chat.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "chatMessages")
@JsonIgnoreProperties(ignoreUnknown = true)
@CompoundIndex(def = "{'chatRoomId': 1, 'timestamp': 1}")
public class ChatMessage {

    @Id
    private String id;
    private String senderId;
    private String chatRoomId;
    private Instant timestamp;
    private String content;
    private MessageStatus status;
    private boolean isEncrypted;
    private User user;
    private Set<String> deletedForUsers;
    private Boolean deletedForEveryone;
    private Map<String, Set<String>> deliveryReceiptsByTime = new TreeMap<>();
    private Map<String, Set<String>> readReceiptsByTime = new TreeMap<>();

    public void addUserToReadReceipt(String timestamp,String userId){
        if(!readReceiptsByTime.containsKey(userId)){
            Set<String> userIds = readReceiptsByTime.getOrDefault(timestamp,new HashSet());
            userIds.add(userId);
            readReceiptsByTime.put(timestamp,userIds);
        }
        return;
    }

    public void addUserToDeliveredReceipt(String timestamp,String userId){
        if(!deliveryReceiptsByTime.containsKey(userId)) {
            Set<String> userIds = deliveryReceiptsByTime.getOrDefault(timestamp, new HashSet());
            userIds.add(userId);
            deliveryReceiptsByTime.put(timestamp, userIds);
        }
        return;
    }

    public void setMessageStatus(MessageStatus newStatus) {
        status = newStatus;
    }

    public void addUserToDeletedForUsers(String userId) {
        if(deletedForUsers==null){
            deletedForUsers=new HashSet<>();
        }
        deletedForUsers.add(userId);
    }
}
