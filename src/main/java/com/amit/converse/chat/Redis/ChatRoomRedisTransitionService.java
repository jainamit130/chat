package com.amit.converse.chat.Redis;

import com.amit.converse.chat.Interface.ITransition;
import com.amit.converse.chat.dto.OnlineUsers.IOnlineUsersDTO;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.MessageProcessor.IReadProcessingService;
import com.amit.converse.chat.service.Redis.RedisChatRoomService;
import com.amit.converse.chat.service.User.UserChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public abstract class ChatRoomRedisTransitionService implements ITransition {
    @Autowired
    @Lazy
    private UserChatService userChatService;
    @Autowired
    private RedisChatRoomService redisChatRoomService;
    @Autowired
    private IReadProcessingService readProcessingService;

    public abstract IOnlineUsersDTO transitAndGetOnlineUsers();

    @Override
    public void transit() {
        User user = userChatService.getContextUser();
        ChatRoom chatRoom = userChatService.getContextChatRoom();
        redisChatRoomService.addUserToChatRoom(chatRoom,user);
        readProcessingService.read(user);
    }
}
