package com.amit.converse.chat.model;

import com.amit.converse.chat.model.MetaData.MessageMetaData;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "chatMessages")
@CompoundIndex(def = "{'chatRoomId': 1, 'timestamp': 1}")
public abstract class Message {
    @Id
    private String id;
    private String senderId;
    private String chatRoomId;
    private String content;
    private Instant timestamp;
    private MessageMetaData metaData;

    public void deleteMessage(String userId) {
        metaData.addUserToDeletedForUsers(userId);
    }

    public abstract void readMessage(String timestamp,String userId);
    public abstract void deliverMessage(String timestamp,String userId);
}



/*
* Message is processed
*
*
*
*
* */
