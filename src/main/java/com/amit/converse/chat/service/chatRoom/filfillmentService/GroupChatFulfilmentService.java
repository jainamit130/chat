package com.amit.converse.chat.service.chatRoom.filfillmentService;

import com.amit.converse.chat.Redis.GroupChatRedisTransitionService;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.ChatRooms.GroupChat;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.User.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class GroupChatFulfilmentService extends ChatRoomFulfilmentService {

    @Autowired
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

    @Override
    public void fillIsExited(ChatRoom chatRoom) {
        GroupChat groupChat = (GroupChat) chatRoom;
        User user = UserService.getUserContext();
        if(user==null) return;
        groupChat.setIsExited(user.isExited(groupChat.getId()));
    }
}
