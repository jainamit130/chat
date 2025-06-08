package com.amit.converse.chat.service.MessageService.DeleteMessageService;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.context.ChatRoom.ChatContext;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.service.User.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeleteChatMessageForMeService extends DeleteChatMessageService {

    public void deleteMessageForMe(List<String> messageIds) {
        List<ChatMessage> messages = messageRepository.findAllById(messageIds);
        String userId = UserService.getUserContext().getUserId();
        IChatRoom chatRoom = ChatContext.getChatRoom();
        List<ChatMessage> messagesToSave = new ArrayList<>();
        for(ChatMessage message: messages) deleteMessageForUser(chatRoom,userId,message,new ArrayList<>(messagesToSave));
        saveDeletedMessages(messagesToSave);
    }
}
