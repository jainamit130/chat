package com.amit.converse.chat.service;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.context.ChatContext;
import com.amit.converse.chat.context.UserContext;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.ChatRoom.ChatService;
import com.amit.converse.chat.service.MessageService.DeleteMessageService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
@AllArgsConstructor
public class ClearChatService {
    private final ChatContext context;
    private final UserContext userContext;
    private final ChatService chatService;
    private final DeleteMessageService deleteMessageService;

    public void clearChat() {
        String contextUserId = userContext.getUserId();
        IChatRoom chatRoom = context.getChatRoom();
        Instant lastClearedTimestamp = chatRoom.getUserFetchStartTime(contextUserId);
        chatService.clearChat(contextUserId);
        deleteMessageService.deleteMessagesForUserFromTillNow(chatRoom,lastClearedTimestamp,contextUserId);
    }
}
