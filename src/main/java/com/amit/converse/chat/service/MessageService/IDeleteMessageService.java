package com.amit.converse.chat.service.MessageService;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.Messages.Message;

public interface IDeleteMessageService {
    void deleteMessageForEveryone(IChatRoom chatRoom, String userId, Message message);
    void deleteMessageForMe(IChatRoom chatRoom, String userId, Message message);
}
