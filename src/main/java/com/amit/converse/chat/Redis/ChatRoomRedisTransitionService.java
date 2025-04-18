package com.amit.converse.chat.Redis;

import com.amit.converse.chat.Interface.ITransition;
import com.amit.converse.chat.context.ChatRoom.ChatContext;
import com.amit.converse.chat.context.User.UserContext;
import com.amit.converse.chat.dto.OnlineUsers.IOnlineUsersDTO;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.MessageProcessor.ReadProcessingService;
import com.amit.converse.chat.service.Redis.RedisWriteService;
import com.amit.converse.chat.service.User.UserChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public abstract class ChatRoomRedisTransitionService implements ITransition {
    @Autowired
    private RedisWriteService redisWriteService;
    @Autowired
    private ReadProcessingService readProcessingService;

    public abstract IOnlineUsersDTO transitAndGetOnlineUsers();

    @Override
    public void transit() {
        User user = UserContext.getUser();
        ChatRoom chatRoom = (ChatRoom) ChatContext.getChatRoom();
        redisWriteService.addUserToChatRoom(chatRoom,user);
        readProcessingService.read(user);
    }
}
