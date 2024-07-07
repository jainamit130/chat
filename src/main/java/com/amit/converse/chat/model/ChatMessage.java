package com.amit.converse.chat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "chatMessages")
public class ChatMessage {

    @Id
    private String id;
    private String senderId;
    private String chatRoomId;
    private Instant timestamp;
    private String content;
    private boolean isEncrypted;
}
