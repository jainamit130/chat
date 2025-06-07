package com.amit.converse.chat.dto.OnlineUsers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupChatOnlineUsersDTO implements IOnlineUsersDTO {
    List<String> onlineUsers = new ArrayList<>();

}
