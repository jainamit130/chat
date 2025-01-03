package com.amit.converse.chat.dto;

import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.Enums.ChatRoomType;
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
    private ChatMessage latestMessage;
}
