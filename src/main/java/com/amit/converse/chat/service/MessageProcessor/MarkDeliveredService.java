package com.amit.converse.chat.service.MessageProcessor;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.Messages.Message;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.MessageService.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class MarkDeliveredService {

    @Autowired
    private ChatMessageService chatMessageService;

    public void deliver(List<IChatRoom> chatRooms, User user) {
        List<Message> toBeDeliveredMessages = new ArrayList<>();
        for(IChatRoom chatRoom: chatRooms) {
            Instant lastDeliveredTimestamp = user.getLastSeenTimestamp();
            toBeDeliveredMessages.addAll(chatMessageService.getMessagesOfChatFrom(chatRoom.getId(), user.getUserId(),lastDeliveredTimestamp));
        }

    }

    public void deliver(Message message) {

    }
}
