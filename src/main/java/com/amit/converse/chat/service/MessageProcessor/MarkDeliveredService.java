package com.amit.converse.chat.service.MessageProcessor;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.Messages.ChatMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MarkDeliveredService extends MarkService {

    @Override
    public Integer markMessage(ChatMessage message, String timestamp, String userId) {
        return message.deliverMessage(timestamp,userId);
    }

    @Override
    public List<String> getActiveUserIds(IChatRoom chatRoom) {
        setOnlineUserIds(new ArrayList<>(redisReadService.filterOnlineUsers(chatRoom)));
        return getOnlineUserIds();
    }
}
