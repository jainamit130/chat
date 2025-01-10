package com.amit.converse.chat.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class CreateGroupRequest {
    private String groupName;
    private List<String> userIds;
}
