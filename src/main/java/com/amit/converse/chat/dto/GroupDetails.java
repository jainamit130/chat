package com.amit.converse.chat.dto;

import com.amit.converse.chat.model.OnlineStatus;
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
public class GroupDetails {
    private String chatRoomId;
    private List<UserEventDTO> members;
}
