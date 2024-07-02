package com.amit.converse.chat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {

    private String id;
    private String content;
    private String sender;
    private String receiver;
    private Instant timeStamp;
    private MessageType type;
}
