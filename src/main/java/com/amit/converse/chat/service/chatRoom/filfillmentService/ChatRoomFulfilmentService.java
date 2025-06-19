package com.amit.converse.chat.service.chatRoom.filfillmentService;

import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.Messages.Message;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.MessageService.ChatMessageService;
import com.amit.converse.chat.service.User.UserChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public abstract class ChatRoomFulfilmentService {

    @Autowired
    private ChatMessageService<ChatRoom> chatMessageService;

    @Autowired
    protected UserChatService userChatService;

    // Latest Message
    public void fillLatestMessage(ChatRoom chatRoom,User user) {
        Message latestMessage = chatMessageService.getLatestMessage(chatRoom, user);
        chatRoom.setLatestMessage(latestMessage);
    }

    protected void fillUnreadMessageCount(ChatRoom chatRoom,User user) {
        chatRoom.setUnreadMessageCount(userChatService.getUnreadMessageCount(chatRoom,user));
    }

    public abstract void fillTransitionService(ChatRoom chatRoom);

    public abstract void fillName(ChatRoom chatRoom,User user);

    public final void fulfill(ChatRoom chatRoom, User user) {
        fillIsExited(chatRoom,user);
        fillTransitionService(chatRoom);
        fillLatestMessage(chatRoom,user);
        fillUnreadMessageCount(chatRoom,user);
        fillName(chatRoom,user);
    }

    protected void fillIsExited(ChatRoom chatRoom,User user) {
    }

}
