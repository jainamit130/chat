package com.amit.converse.chat.dto;

import com.amit.converse.chat.model.Enums.ConnectionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDetails {
    private String userId;
    private String username;
    // Self chat if the requester is the loggedIn user else Direct Chat
    private String commonChatId;
    // if the requester is the loggedIn user then empty
    private List<String> commonGroupChatIds;
    private String userStatus;
    private Instant lastSeenTimestamp;
    private ConnectionStatus status;
}
