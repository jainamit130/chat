package com.amit.converse.chat.model.Messages;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

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

    public Message(String content) {
        this.content = content;
    }

    public Message(String content,Instant timestamp) {
        this.timestamp = timestamp;
        this.content = content;
    }

    public abstract Integer readMessage(Instant timestamp,String userId);

    public abstract Integer deliverMessage(Instant timestamp,String userId);
}
