package com.amit.converse.chat.dto.OnlineUsers;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Builder
@Data
public class SelfChatOnlineUsersDTO implements IOnlineUsersDTO {
    Instant lastSeenTimestamp;
}