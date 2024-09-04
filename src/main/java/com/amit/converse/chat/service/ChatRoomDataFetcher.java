package com.amit.converse.chat.service;

import com.amit.converse.chat.model.ChatRoom;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ChatRoomDataFetcher implements DataFetcher<List<ChatRoom>> {

    @Autowired
    private GroupService chatRoomService;  // Service where getChatRoomsOfUser is defined

    @Override
    public List<ChatRoom> get(DataFetchingEnvironment environment) {
        String userId = environment.getArgument("userId");
        return chatRoomService.getChatRoomsOfUser(userId);
    }
}
