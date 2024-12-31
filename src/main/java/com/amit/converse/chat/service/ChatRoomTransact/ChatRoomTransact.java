package com.amit.converse.chat.service.ChatRoomTransact;

import com.amit.converse.chat.Interface.ITransact;
import com.amit.converse.chat.service.ChatRoom.ChatService;
import com.amit.converse.chat.service.UserService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ChatRoomTransact implements ITransact {

    private final UserService userService;
    private final ChatService chatService;
    private final NotificationService notificationService;

    @Override
    public final void transact() {
        userService.transact()
    }
}
