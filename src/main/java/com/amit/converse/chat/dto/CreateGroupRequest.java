package com.amit.converse.chat.dto;

import com.amit.converse.chat.model.ChatRoomType;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateGroupRequest {
    private String groupName;
    private ChatRoomType chatRoomType;
    private String createdById;
    private List<String> members;
}
