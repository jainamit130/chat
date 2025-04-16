package com.amit.converse.chat.service.MessageService;

import com.amit.converse.chat.Interface.ITransactable;
import com.amit.converse.chat.context.User.UserContext;
import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.service.MessageService.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupChatMessageService extends ChatMessageService<ITransactable> {

    @Override
    protected void authoriseSender() {
        if(UserContext.getUser().isExited(chatService.getContextChatRoom().getId()))
            throw new ConverseException("User is not part of the group!");
    }
}
