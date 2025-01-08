package com.amit.converse.chat.service.ChatRoom;

import com.amit.converse.chat.Interface.ITransactable;
import com.amit.converse.chat.context.ChatContext;
import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.service.MessageProcessingService;
import com.amit.converse.chat.service.MessageService.SaveMessageService;
import com.amit.converse.chat.service.Notification.ChatNotificationService;
import org.springframework.stereotype.Service;

@Service
public class GroupMessageService extends SendMessageService {

    private final ChatContext<ITransactable> chatContext;

    public GroupMessageService(ChatContext chatContext, MessageProcessingService messageProcessingService, SaveMessageService saveMessageService, ChatService chatService, ChatNotificationService notificationService, ChatContext<ITransactable> chatContext1) {
        super(chatContext, messageProcessingService, saveMessageService, chatService, notificationService);
        this.chatContext = chatContext1;
    }

    @Override
    protected void authoriseSender() {
        ITransactable chatRoom = chatContext.getChatRoom();
        if(chatRoom.isExited(chatContext.getUser().getUserId()))
            throw new ConverseException("User is not part of the group!");
    }
}
