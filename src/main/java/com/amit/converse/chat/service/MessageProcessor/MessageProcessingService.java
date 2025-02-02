package com.amit.converse.chat.service.MessageProcessor;

import com.amit.converse.chat.service.ChatRoom.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageProcessingService {
    @Autowired
    private ChatService chatService;


}
