package com.amit.converse.chat.service.chatRoom.filfillmentService;

import com.amit.converse.chat.Redis.DirectChatRedisTransitionService;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.User.DirectChatUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DirectChatFulfilmentService extends ChatRoomFulfilmentService {

    @Autowired
    private DirectChatUserService directChatUserService;

    @Autowired
    private DirectChatRedisTransitionService directChatRedisTransitionService;

    @Override
    public void fillTransitionService(ChatRoom chatRoom) {
        chatRoom.setChatRoomRedisTransitionService(directChatRedisTransitionService);
    }

    @Override
    public void fillName(ChatRoom chatRoom) {
        User counterPartUser = directChatUserService.getCounterPartUser(chatRoom.getAllUserIds());
        chatRoom.setName(counterPartUser.getDisplayName());
    }
}
