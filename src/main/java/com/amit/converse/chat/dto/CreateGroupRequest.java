package com.amit.converse.chat.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateGroupRequest {
    private String groupName;
    private String createdByUserId;
}
