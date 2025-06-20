package com.amit.converse.chat.service.MessageService;

import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.Messages.Message;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    public void updateLatestMessagesOfMembers(Message message, ChatRoom chatRoom, List<String> userIds) {
        for(String userId : userIds) {
            updateLatestMessageOfUser(message,chatRoom,userId);
        }
    }

    public void updateLatestMessageOfUser(Message message, ChatRoom chatRoom, String userId) {
        chatRoom.updateLatestMessageOfMember(userId,message);
    }

}
