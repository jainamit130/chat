package com.amit.converse.chat.controller;

import com.amit.converse.chat.model.ChatRoom;
import com.amit.converse.chat.repository.ChatRoomRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;

    @GetMapping("/user/{userId}/rooms")
    public List<ChatRoom> getChatRoomsForUser(@PathVariable String userId) {
        return chatRoomRepository.findByUserIdsContains(userId);
    }
}
