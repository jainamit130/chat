package com.amit.converse.chat.model.ChatRooms;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class GroupChat extends ChatRoom {
    @NotBlank
    private String name;
    private String createdBy;
    private Instant createdAt;
    // Exit Group Feature Only For Groups
    private transient Boolean isExited;
    private Map<String,Instant> exitedMembers;
}
