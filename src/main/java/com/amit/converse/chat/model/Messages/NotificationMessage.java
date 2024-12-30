package com.amit.converse.chat.model.Messages;

import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "chatMessages")
@CompoundIndex(def = "{'chatRoomId': 1, 'timestamp': 1}")
@EqualsAndHashCode(callSuper = true)
public class NotificationMessage extends Message {

    @Override
    public void readMessage(String timestamp, String userId) {

    }

    @Override
    public void deliverMessage(String timestamp, String userId) {

    }
}
