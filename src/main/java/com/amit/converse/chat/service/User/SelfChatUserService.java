package com.amit.converse.chat.service.User;

import com.amit.converse.chat.dto.OnlineUsers.IOnlineUsersDTO;
import com.amit.converse.chat.model.ChatRooms.SelfChat;
import com.amit.converse.chat.service.ChatRoom.SelfChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SelfChatUserService extends UserChatService<SelfChat> {

    @Autowired
    private SelfChatService selfChatService;

    @Override
    public IOnlineUsersDTO getOnlineUsersDTO(List<String> onlineUserIdsOfChat) {
        return null;
    }

    public SelfChat getSelfChat() {
        return selfChatService.getChat(getContextUser());
    }
}
