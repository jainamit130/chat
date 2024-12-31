package com.amit.converse.chat.service.ChatRoom;

import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.Redis.RedisChatRoomService;
import com.amit.converse.chat.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class JoinService {
    private final UserService userService;
    private final ChatService chatService;
    private final NotificationService notificationService;

    public void joinChatRoom(ChatRoom chatRoom, List<User> user) {
        user.joinChatRoom(chatRoom.getId());
        chatService.
    }
}
