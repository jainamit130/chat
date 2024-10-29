package com.amit.converse.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.Set;

@Builder
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class GroupStatusResponse {
    Set<String> onlineUsers;
    Instant lastSeenTimestamp;
}
