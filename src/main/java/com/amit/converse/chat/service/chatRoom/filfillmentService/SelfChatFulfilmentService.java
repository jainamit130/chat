package com.amit.converse.chat.service.chatRoom.filfillmentService;

import com.amit.converse.chat.Redis.SelfChatRedisTransitionService;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class SelfChatFulfilmentService extends ChatRoomFulfilmentService {

    @Autowired
    private SelfChatRedisTransitionService selfChatRedisTransitionService;

    @Override
    public void fillTransitionService(ChatRoom chatRoom) {
        chatRoom.setChatRoomRedisTransitionService(selfChatRedisTransitionService);
    }

    @Override
    public void fillName(ChatRoom chatRoom, User user) {

    }
}
