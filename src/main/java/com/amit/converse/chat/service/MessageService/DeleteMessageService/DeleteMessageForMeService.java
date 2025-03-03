package com.amit.converse.chat.service.MessageService.DeleteMessageService;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.Messages.ChatMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeleteMessageForMeService extends DeleteMessageService {

    public void deleteMessageForMe(List<String> messageIds) {
        List<ChatMessage> messages = messageRepository.findAllById(messageIds);
        String userId = userChatService.getContextUser().getUserId();
        IChatRoom chatRoom = userChatService.getContextChatRoom();
        List<ChatMessage> messagesToSave = new ArrayList<>();
        for(ChatMessage message: messages) deleteMessageForUser(chatRoom,userId,message,messagesToSave);
        saveDeletedMessages(messagesToSave);
    }
}
