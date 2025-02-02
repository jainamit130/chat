package com.amit.converse.chat.service.ChatRoom;

import com.amit.converse.chat.Interface.ITransactable;
import com.amit.converse.chat.context.UserContext;
import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.service.MessageService.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupMessageService extends MessageService<ITransactable> {

    @Autowired
    private UserContext userContext;

    @Override
    protected void authoriseSender() {
        if(userContext.getUser().isExited(chatContext.getChatRoomId()))
            throw new ConverseException("User is not part of the group!");
    }
}
