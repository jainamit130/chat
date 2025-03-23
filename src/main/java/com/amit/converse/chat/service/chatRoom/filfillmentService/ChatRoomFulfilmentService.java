package com.amit.converse.chat.service.chatRoom.filfillmentService;

import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.service.MessageService.ChatMessageService;
import com.amit.converse.chat.service.User.UserChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public abstract class ChatRoomFulfilmentService {

    @Autowired
    private ChatMessageService<ChatRoom> chatMessageService;

    @Autowired
    private UserChatService userChatService;

    // Latest Message
    public void fillLatestMessage(ChatRoom chatRoom) {
        chatRoom.setLatestMessage(chatMessageService.getLatestMessage(chatRoom));
    }

    private void fillUnreadMessageCount(ChatRoom chatRoom) {
        chatRoom.setUnreadMessageCount(userChatService.getUnreadMessageCount(chatRoom));
    }

    public abstract void fillTransitionService(ChatRoom chatRoom);

    public abstract void fillName(ChatRoom chatRoom);

    public final void fulfill(ChatRoom chatRoom) {
        fillTransitionService(chatRoom);
        fillLatestMessage(chatRoom);
        fillUnreadMessageCount(chatRoom);
        fillName(chatRoom);
    }

}
