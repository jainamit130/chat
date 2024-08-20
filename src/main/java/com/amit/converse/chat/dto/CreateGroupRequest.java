package com.amit.converse.chat.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateGroupRequest {
    private String groupName;
    private String createdByUserId;
    private List<String> members;
}
