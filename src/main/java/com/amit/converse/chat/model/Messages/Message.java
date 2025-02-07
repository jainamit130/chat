package com.amit.converse.chat.model.Messages;

import com.amit.converse.chat.model.MetaData.MessageMetaData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Document(collection = "messages")
public abstract class Message {
    @Id
    private String id;
    protected String chatRoomId;
    protected String content;
    protected Instant timestamp;
    protected MessageMetaData metaData;

    public abstract Integer readMessage(String timestamp,String userId);

    public abstract Integer deliverMessage(String timestamp,String userId);

    public void deleteMessage(String userId) {
        metaData.addUserToDeletedForUsers(userId);
    }

    public Integer getDeletedForMembersCount() {
        return metaData.getDeletedForUsersCount();
    }
}
