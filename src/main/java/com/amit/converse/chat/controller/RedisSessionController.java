package com.amit.converse.chat.controller;

import com.amit.converse.chat.Redis.ChatRoomRedisITransition;
import com.amit.converse.chat.dto.OnlineUsersDto;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class RedisSessionController {
    private final UserService userService;

    private void transit(String userId) {
        User user = userService.getUser(userId);
        user.transit();
        userService.saveUser(user);
    }

    @PostMapping("/state/transit/{userId}")
    public ResponseEntity transitState(@PathVariable String userId) {
        transit(userId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/save/activeChatRoom")
    public ResponseEntity<OnlineUsersDto> saveActiveChatRoom(@ModelAttribute ChatRoomRedisITransition chatRoomRedisTransition) {
        return new ResponseEntity<OnlineUsersDto>(chatRoomRedisTransition.transitAndGetOnlineUsers(), HttpStatus.OK);
    }

}
