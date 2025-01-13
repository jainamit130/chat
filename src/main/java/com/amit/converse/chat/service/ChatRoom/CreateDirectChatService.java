package com.amit.converse.chat.service.ChatRoom;

import com.amit.converse.chat.context.ChatContext;
import com.amit.converse.chat.context.UserContext;
import com.amit.converse.chat.dto.CreateDirectChatRequest;
import com.amit.converse.chat.model.ChatRooms.DirectChat;
import com.amit.converse.chat.model.Enums.ChatRoomType;
import com.amit.converse.chat.service.Notification.UserNotificationService;
import com.amit.converse.chat.service.User.DirectChatUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;

@Service
@AllArgsConstructor
public class CreateDirectChatService {

    private final UserContext userContext;
    private final ChatContext chatContext;
    private final DirectChatService directChatService;
    private final DirectChatUserService directChatUserService;

    public static DirectChat getNewDirectChat(String partUserId, String counterPartUserId) {
        DirectChat directChat = DirectChat.builder().build();
        directChat.setUserIds(Arrays.asList(partUserId,counterPartUserId));
        directChat.setDeletedForUsers(new HashSet<>() {{
            add(partUserId);
            add(counterPartUserId);
        }});
        directChat.setChatRoomType(ChatRoomType.DIRECT);
        return directChat;
    }

    // Returns the DirectChat id
    public String create(CreateDirectChatRequest directChatRequest) throws InterruptedException {
        directChatRequest.setPrimaryUserId(userContext.getUserId());
        directChatService.processCreation(directChatRequest);
        directChatUserService.processCreation();
        return chatContext.getChatRoom().getId();
    }
}
