package com.amit.converse.chat.service.MessageProcessor;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.service.MessageService.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarkDeliveredService {

    @Autowired
    private MessageService messageService;

    public void deliver(List<IChatRoom> chatRooms, String userId) {

    }
}
