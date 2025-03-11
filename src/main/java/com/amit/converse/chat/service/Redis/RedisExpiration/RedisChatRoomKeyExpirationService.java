package com.amit.converse.chat.service.Redis.RedisExpiration;

import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.service.ChatRoom.ChatService;
import com.amit.converse.chat.service.Redis.RedisWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class RedisChatRoomKeyExpirationService extends RedisExpirationService {
    @Autowired
    @Lazy
    private ChatService chatService;

    @Autowired
    @Lazy
    private RedisWriteService redisWriteService;

    @Override
    public void expire(String keyValue) {
        try {
            String chatRoomId = redisKeyService.extractValue(keyValue);
            ChatRoom chatRoom = chatService.getChatRoomById(chatRoomId);
            redisWriteService.removeUserFromChatRoom(chatRoom,userService.getUserContext());
        } catch (ConverseException converseException) {
            System.out.println(converseException.getMessage());
        }
    }
}
