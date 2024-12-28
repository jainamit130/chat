package com.amit.converse.chat.model;

import com.amit.converse.chat.model.MetaData.MessageMetaData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public abstract class Message {
    @Id
    private String id;
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
