package com.amit.converse.chat.service.ChatRoom;

import com.amit.converse.chat.Interface.ITransactable;
import com.amit.converse.chat.context.UserContext;
import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.service.MessageService.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupChatMessageService extends ChatMessageService<ITransactable> {

    @Autowired
    private UserContext userContext;

    @Override
    protected void authoriseSender() {
        if(userContext.getUser().isExited(chatService.getContextChatRoom().getId()))
            throw new ConverseException("User is not part of the group!");
    }
}
