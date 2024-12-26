package com.amit.converse.chat.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "chatMessages")
@EqualsAndHashCode(callSuper = true)
public class NotificationMessage extends Message {

    @Override
    public void readMessage(String timestamp, String userId) {

    }

    @Override
    public void deliverMessage(String timestamp, String userId) {

    }
}
