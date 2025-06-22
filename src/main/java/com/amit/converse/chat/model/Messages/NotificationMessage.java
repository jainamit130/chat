package com.amit.converse.chat.model.Messages;

import com.amit.converse.chat.model.MetaData.NotificationMessageMetaData;
import lombok.*;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = false)
@Document(collection = "messages")
@TypeAlias("NotificationMessage")
@CompoundIndex(def = "{'chatRoomId': 1, 'timestamp': 1}")
public class NotificationMessage extends Message {

    private NotificationMessageMetaData messageMetaData;

    @Transient
    private String __typename = "NotificationMessage";

    public NotificationMessage() {
        super("There are no messages");
        this.messageMetaData = new NotificationMessageMetaData();
    }

    @Override
    public Integer readMessage(Instant timestamp, String userId) {
        return 0;
    }

    @Override
    public Integer deliverMessage(Instant timestamp, String userId) {
        return 0;
    }

    public void deleteMessage(String userId) {
        messageMetaData.addUserToDeletedForUsers(userId);
    }

    @Override
    public Integer getDeletedForMembersCount() {
        return messageMetaData.getDeletedForUsersCount();
    }

    @Override
    public void incrementMemberCount(int incrementCount) {

    }
}
