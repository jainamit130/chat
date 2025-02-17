package com.amit.converse.chat.service.ChatRoom.FilfillmentService;

import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.User.DirectChatUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class DirectChatFulfilmentService extends ChatRoomFulfilmentService {

    @Autowired
    @Lazy
    private DirectChatUserService directChatUserService;

    @Override
    public void fillName(ChatRoom chatRoom) {
        User counterPartUser = directChatUserService.getCounterPartUser(chatRoom.getUserIds());
        chatRoom.setName(counterPartUser.getUsername());
    }
}
