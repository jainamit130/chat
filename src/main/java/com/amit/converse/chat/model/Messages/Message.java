package com.amit.converse.chat.model.Messages;

import com.amit.converse.chat.model.MetaData.MessageMetaData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "messages")
public abstract class Message {
    @Id
    private String id;
    protected String chatRoomId;
    protected String content;
    protected Instant timestamp;
    protected MessageMetaData messageMetaData;

    public Message(String content) {
        this.content = content;
    }

    public Message(String content,MessageMetaData messageMetaData) {
        this.messageMetaData = messageMetaData;
        this.content = content;
    }

    public abstract Integer readMessage(String timestamp,String userId);

    public abstract Integer deliverMessage(String timestamp,String userId);

    public void deleteMessage(String userId) {
        messageMetaData.addUserToDeletedForUsers(userId);
    }

    public Integer getDeletedForMembersCount() {
        return messageMetaData.getDeletedForUsersCount();
    }
}
