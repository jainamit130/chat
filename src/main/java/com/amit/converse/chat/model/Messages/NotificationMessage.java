package com.amit.converse.chat.model.Messages;

import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Document(collection = "messages")
@CompoundIndex(def = "{'chatRoomId': 1, 'timestamp': 1}")
public class NotificationMessage extends Message {

    @Override
    public Integer readMessage(String timestamp, String userId) {
        return 0;
    }

    @Override
    public Integer deliverMessage(String timestamp, String userId) {
        return 0;
    }
}
