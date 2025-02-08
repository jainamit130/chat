package com.amit.converse.chat.service.MessageProcessor;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.ChatRoom.ChatService;
import com.amit.converse.chat.service.Redis.RedisReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReadProcessingService implements readProcessor {

    @Autowired
    private ChatService chatService;

    @Autowired
    private RedisReadService redisReadService;

    @Autowired
    private MarkReadService markReadService;

    @Override
    public void read(User user) {
        // all undelivered messages in all chatRooms must be marked delivered
        List<IChatRoom> chatRooms = chatService.getChatRoomsByIds(List.of(user.getChatRoomIds()));
        for(IChatRoom chatRoom:chatRooms) {
            markReadService.mark(chatRoom,user);
        }
        markReadService.saveAllMarkedMessages();
    }

    @Override
    public void read(ChatMessage message) {
        IChatRoom chatRoom = chatService.getChatRoomById(message.getChatRoomId());
        markReadService.mark(chatRoom,message);
        markReadService.saveAllMarkedMessages();
    }
}
