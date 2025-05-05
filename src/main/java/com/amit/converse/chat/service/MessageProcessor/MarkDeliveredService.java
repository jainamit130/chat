package com.amit.converse.chat.service.MessageProcessor;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.dto.Notification.MessageDeliveredNotification;
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
public class MarkDeliveredService extends MarkService {

    @Autowired
    private RedisReadService redisReadService;

    @Autowired
    private ChatMessageService chatMessageService;

    public MarkDeliveredService(RedisReadService redisReadService, ChatMessageService chatMessageService) {
        super(redisReadService, chatMessageService);
    }

    @Override
    public Integer markMessage(ChatMessage message, String timestamp, String userId) {
        return message.deliverMessage(timestamp,userId);
    }

    @Override
    public List<String> getActiveUserIds(IChatRoom chatRoom) {
        setOnlineUserIds(new ArrayList<>(redisReadService.filterOnlineUsers(chatRoom)));
        return getOnlineUserIds();
    }

    @Override
    public void sendMessageMarkedNotification(String chatRoomId, String senderId, List<String> messageIds) {
        chatMessageService.sendMessageMarkedNotification(senderId,new MessageDeliveredNotification(chatRoomId,messageIds));
    }

    @Override
    public void processMessage(ChatMessage message) {
        message.deliverMessage();
    }

    @Override
    public Instant getLastVisitedTimestamp(IChatRoom chatRoom, User user) {
        return user.getLastSeenTimestamp();
    }

}
