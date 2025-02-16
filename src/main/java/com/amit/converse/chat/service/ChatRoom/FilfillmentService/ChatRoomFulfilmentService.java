package com.amit.converse.chat.service.ChatRoom.FilfillmentService;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.service.MessageService.ChatMessageService;
import com.amit.converse.chat.service.User.UserChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatRoomFulfilmentService {

    @Autowired
    private ChatMessageService<ChatRoom> chatMessageService;

    @Autowired
    private UserChatService userChatService;

    // Latest Message
    public void fillLatestMessage(IChatRoom chatRoom) {
        chatRoom.setLatestMessage(chatMessageService.getLatestMessage(chatRoom));
    }

    private void fillUnreadMessageCount(IChatRoom chatRoom) {
        userChatService.getUnreadMessageCount(chatRoom);
    }

    public final void fulfill(IChatRoom chatRoom) {
        fillLatestMessage(chatRoom);
        fillUnreadMessageCount(chatRoom);

    }

}
