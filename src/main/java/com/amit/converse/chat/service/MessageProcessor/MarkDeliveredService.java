package com.amit.converse.chat.service.MessageProcessor;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.service.MessageService.ChatMessageService;
import com.amit.converse.chat.service.Redis.RedisReadService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Scope("prototype")
public class MarkDeliveredService extends MarkService {

    @Autowired
    public MarkDeliveredService(RedisReadService redisReadService, @Lazy ChatMessageService chatMessageService) {
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
    public void processMessage(ChatMessage message) {
        message.deliverMessage();
    }
}
