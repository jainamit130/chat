package com.amit.converse.chat.dto;

import com.amit.converse.chat.model.ConnectionStatus;
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
    private String id;
    private String username;
    private String commonIndividualChatId;
    private List<String> commonChatRoomIds;
    private String userStatus;
    private Instant lastSeenTimestamp;
    private ConnectionStatus status;
}
