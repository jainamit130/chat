package com.amit.converse.chat.controller;

import com.amit.converse.chat.Redis.DirectChatRedisTransitionService;
import com.amit.converse.chat.Redis.GroupChatRedisTransitionService;
import com.amit.converse.chat.dto.OnlineUsers.IOnlineUsersDTO;
import com.amit.converse.chat.service.User.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/converse/users/")
public class RedisSessionController {
    private final UserService userService;
    private final DirectChatRedisTransitionService directChatRedisTransitionService;
    private final GroupChatRedisTransitionService groupChatRedisTransitionService;
    @Value("${Redis.Key.Timeout}")
    private Long redisKeyTimeout;

    @PostMapping("/state/active")
    public ResponseEntity activateUser() {
        System.out.println(userService.getUserContext().getUsername() + " is active with a TTL of "+redisKeyTimeout+" seconds");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
