package com.amit.converse.chat.service.User;

import com.amit.converse.chat.dto.OnlineUsers.IOnlineUsersDTO;

import java.util.List;

public interface IChatUserService {
    IOnlineUsersDTO getOnlineUsersDTO(List<String> onlineUserIdsOfChat);
}
