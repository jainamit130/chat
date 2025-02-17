package com.amit.converse.chat.service.ChatRoom.FilfillmentService;

import com.amit.converse.chat.model.Enums.ChatRoomType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class FulfilmentConfig {

    @Bean
    public Map<ChatRoomType, ChatRoomFulfilmentService> serviceMap(
            DirectChatFulfilmentService directChatFulfilmentService,
            GroupChatFulfilmentService groupChatFulfilmentService,
            SelfChatFulfilmentService selfChatFulfilmentService) {

        return Map.of(
                ChatRoomType.DIRECT, directChatFulfilmentService,
                ChatRoomType.GROUP, groupChatFulfilmentService,
                ChatRoomType.SELF, selfChatFulfilmentService
        );
    }
}
