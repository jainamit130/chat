package com.amit.converse.chat.service.chatRoom.MessageFilters;

import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.Messages.Message;
import com.amit.converse.chat.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BlindPeriodFilter extends IFilter {
    public List<Message> filterBlindSpots(ChatRoom chatRoom, User user, List<Message> messages) {
        // dummy no use
        return messages;
    }
}
