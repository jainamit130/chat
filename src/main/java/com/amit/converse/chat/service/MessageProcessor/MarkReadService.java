package com.amit.converse.chat.service.MessageProcessor;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.dto.Notification.MessageReadNotification;
import com.amit.converse.chat.model.Enums.MessageStatus;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.MessageService.ChatMessageService;
import com.amit.converse.chat.service.Redis.RedisReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class MarkReadService extends MarkService {

    @Autowired
    public MarkReadService(RedisReadService redisReadService, ChatMessageService chatMessageService) {
        super(redisReadService, chatMessageService);
    }

    @Override
    public Integer markMessage(ChatMessage message,Instant timestamp, String userId) {
        return message.readMessage(timestamp,userId);
    }

    @Override
    public List<String> getActiveUserIds(IChatRoom chatRoom) {
        List<String> onlineUserIds = getOnlineUserIds();
        if(onlineUserIds==null || onlineUserIds.isEmpty()) {
            return new ArrayList<>(redisReadService.filterActiveUsers(chatRoom));
        }
        return new ArrayList<>(redisReadService.filterActiveUsers(chatRoom,onlineUserIds));
    }

    @Override
    public void sendMessageMarkedNotification(String chatRoomId, String senderId, List<String> messageIds) {
        chatMessageService.sendMessageMarkedNotification(senderId,new MessageReadNotification(chatRoomId,messageIds));
    }

    @Override
    public void processMessage(ChatMessage message) {
        message.readMessage();
    }

    @Override
    public Instant getLastVisitedTimestamp(IChatRoom chatRoom, User user) {
        return chatRoom.getLastVisitedTimestamp(user.getUserId());
    }
}
