package com.amit.converse.chat.controller;

import com.amit.converse.chat.Redis.ChatRoomRedisITransition;
import com.amit.converse.chat.context.UserContext;
import com.amit.converse.chat.dto.OnlineUsersDto;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.User.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/converse/user/")
public class RedisSessionController {
    private final UserContext userContext;
    private final UserService userService;

    private void transit() {
        User user = userService.getUserById(userContext.getUserId());
        user.transit();
        userService.processUserToDB(userContext.getUser());
    }

    @PostMapping("/state/transit")
    public ResponseEntity transitState() {
        transit();
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/save/activeChatRoom")
    public ResponseEntity<OnlineUsersDto> saveActiveChatRoom(@ModelAttribute ChatRoomRedisITransition chatRoomRedisTransition) {
        return new ResponseEntity<OnlineUsersDto>(chatRoomRedisTransition.transitAndGetOnlineUsers(), HttpStatus.OK);
    }

}
