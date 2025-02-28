package com.amit.converse.chat.dto.OnlineUsers;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class GroupChatOnlineUsersDTO implements IOnlineUsersDTO {
    List<String> onlineUsers;
}
