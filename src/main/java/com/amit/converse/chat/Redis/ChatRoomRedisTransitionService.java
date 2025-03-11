package com.amit.converse.chat.Redis;

import com.amit.converse.chat.Interface.ITransition;
import com.amit.converse.chat.dto.OnlineUsers.IOnlineUsersDTO;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.MessageProcessor.IReadProcessingService;
import com.amit.converse.chat.service.Redis.RedisReadService;
import com.amit.converse.chat.service.Redis.RedisWriteService;
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
    private RedisWriteService redisWriteService;
    @Autowired
    private IReadProcessingService readProcessingService;

    public abstract IOnlineUsersDTO transitAndGetOnlineUsers();

    /*
    *
    * if user is in redis chatRoom
    * and the key expire that means chatRoom is active then false
    *
    * if user is in redis chatRoom
    * and the reuqest with chatRoom is made then?
    *
    *
    * */


    @Override
    public void transit() {
        User user = userChatService.getContextUser();
        ChatRoom chatRoom = userChatService.getContextChatRoom();
        redisWriteService.addUserToChatRoom(chatRoom,user);
        readProcessingService.read(user);
    }
}
