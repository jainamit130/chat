package com.amit.converse.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@Builder
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class GroupStatusResponse {
    Long onlineMembersCount;
    Instant lastSeenTimestamp;
}
