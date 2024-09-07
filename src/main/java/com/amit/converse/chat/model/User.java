package com.amit.converse.chat.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "user")
public class User {

    @Id
    private String id;
    private String userId;
    private List<String> chatRoomIds;
    private String username;
    private String password;
    private Instant lastSeenTimestamp;
}