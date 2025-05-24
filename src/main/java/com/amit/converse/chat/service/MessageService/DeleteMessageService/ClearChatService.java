package com.amit.converse.chat.service.MessageService.DeleteMessageService;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.context.ChatRoom.ChatContext;
import com.amit.converse.chat.context.User.UserContext;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.service.User.UserService;
import com.amit.converse.chat.service.chatRoom.ChatService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@AllArgsConstructor
public class ClearChatService {
    private final ChatService chatService;
    private final UserService userService;
    private final DeleteMessageService deleteMessageService;

    public void clearChat(IChatRoom chatRoom) {
        String contextUserId = userService.getUserContext().getUserId();
        Instant lastClearedTimestamp = chatRoom.getUserFetchStartTime(contextUserId);
        chatService.clearChat(contextUserId);
        deleteMessageService.deleteMessagesForUserFromTillNow(chatRoom,lastClearedTimestamp,contextUserId);
    }

    public void clearChatAndSave() {
        IChatRoom chatRoom = chatService.getContextChatRoom();
        clearChat(chatRoom);
        chatService.processChatRoomToDB((ChatRoom) chatRoom);
    }
}
