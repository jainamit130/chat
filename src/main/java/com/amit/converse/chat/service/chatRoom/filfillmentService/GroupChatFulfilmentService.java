package com.amit.converse.chat.service.chatRoom.filfillmentService;

import com.amit.converse.chat.Redis.GroupChatRedisTransitionService;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class GroupChatFulfilmentService extends ChatRoomFulfilmentService {

    @Autowired
    @Lazy
    private GroupChatRedisTransitionService groupChatRedisTransitionService;

    @Override
    public void fillTransitionService(ChatRoom chatRoom) {
        chatRoom.setChatRoomRedisTransitionService(groupChatRedisTransitionService);
    }

    @Override
    public void fillName(ChatRoom chatRoom) {
        // Since the name for GroupChat is persisted and hence the ChatRoom instance must already have the name populated
        return;
    }
}
