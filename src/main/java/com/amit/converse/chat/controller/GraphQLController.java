package com.amit.converse.chat.controller;

import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.model.ChatRoom;
import com.amit.converse.chat.repository.ChatRoomRepository;
import com.amit.converse.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class GraphQLController {

    private final ChatService chatService;

    @QueryMapping
    public List<ChatRoom> getChatRoomsOfUser(@Argument String userId) {
        List<ChatRoom> chatRooms=chatService.getChatRoomsOfUser(userId);
        return chatRooms;
    }

    @QueryMapping
    public List<ChatMessage> getMessagesOfChatRoom(@Argument String chatRoomId){
        List<ChatMessage> chatMessages=chatService.getMessagesOfChatRoom(chatRoomId);
        return chatMessages;
    }

}