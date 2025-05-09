package com.amit.converse.chat.model.Messages;

import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = false)
@Document(collection = "messages")
@CompoundIndex(def = "{'chatRoomId': 1, 'timestamp': 1}")
public class NotificationMessage extends Message {

    public NotificationMessage() {
        super("There are no messages");
    }

    @Override
    public Integer readMessage(Instant timestamp, String userId) {
        return 0;
    }

    @Override
    public Integer deliverMessage(Instant timestamp, String userId) {
        return 0;
    }
}
