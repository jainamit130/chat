package com.amit.converse.chat.resolvers;


import com.amit.converse.chat.model.ChatRoom;
import com.amit.converse.chat.repository.ChatRoomRepository;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class QueryResolver implements GraphQLQueryResolver {

    private final ChatRoomRepository chatRoomRepository;

    public List<ChatRoom> getAllChatRooms(String userId) {
        return chatRoomRepository.findByUserIdsContains(userId);
    }
}
