package com.amit.converse.chat.service.MessageService;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.Interface.ITransactable;
import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.User.UserService;
import com.amit.converse.chat.service.chatRoom.MessageFilters.GroupChatBlindPeriodFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class GroupChatMessageService extends ChatMessageService<ITransactable> {

    @Autowired
    private GroupChatBlindPeriodFilter groupChatBlindPeriodFilter;

    @Override
    protected void authoriseSender() {
        if(UserService.getUserContext().isExited(chatService.getContextChatRoom().getId()))
            throw new ConverseException("User is not part of the group!");
    }

    @Override
    public List<ChatMessage> getMessagesOfChatRoom(ChatRoom chatRoom, User user, Instant fromInstant) {
        return groupChatBlindPeriodFilter.filterBlindSpots(chatRoom,user,super.getMessagesOfChatRoom(chatRoom, user, fromInstant));
    }
}
