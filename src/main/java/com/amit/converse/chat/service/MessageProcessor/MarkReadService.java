package com.amit.converse.chat.service.MessageProcessor;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.Messages.ChatMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MarkReadService extends MarkService {

    @Override
    public Integer markMessage(ChatMessage message,String timestamp, String userId) {
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
}
