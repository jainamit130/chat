package com.amit.converse.chat.service.ChatRoom.FilfillmentService;

import com.amit.converse.chat.Redis.SelfChatRedisTransitionService;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class SelfChatFulfilmentService extends ChatRoomFulfilmentService {

    @Autowired
    @Lazy
    private SelfChatRedisTransitionService selfChatRedisTransitionService;

    @Override
    public void fillTransitionService(ChatRoom chatRoom) {
        chatRoom.setChatRoomRedisTransitionService(selfChatRedisTransitionService);
    }

    @Override
    public void fillName(ChatRoom chatRoom) {

    }
}
