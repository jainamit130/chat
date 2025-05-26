package com.amit.converse.chat.service.MessageService;

import com.amit.converse.chat.Interface.ITransactable;
import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.service.User.UserService;
import org.springframework.stereotype.Service;

@Service
public class GroupChatMessageService extends ChatMessageService<ITransactable> {

    @Override
    protected void authoriseSender() {
        if(UserService.getUserContext().isExited(chatService.getContextChatRoom().getId()))
            throw new ConverseException("User is not part of the group!");
    }
}
