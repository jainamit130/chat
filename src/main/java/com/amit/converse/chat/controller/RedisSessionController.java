package com.amit.converse.chat.controller;

import com.amit.converse.chat.Redis.ChatRoomTransition;
import com.amit.converse.chat.Redis.OfflineRedisSessionTransition;
import com.amit.converse.chat.Redis.OnlineRedisSessionTransition;
import com.amit.converse.chat.dto.OnlineUsersDto;
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

    @PostMapping("/save/lastSeen")
    public ResponseEntity saveLastSeen(@ModelAttribute OnlineRedisSessionTransition onlineSession) {
        onlineSession.transit();
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/update/lastSeen")
    public ResponseEntity updateLastSeen(@ModelAttribute OfflineRedisSessionTransition offlineSession) {
        offlineSession.transit();
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/save/activeChatRoom")
    public ResponseEntity<OnlineUsersDto> saveActiveChatRoom(@ModelAttribute ChatRoomTransition chatRoomTransition) {
        return new ResponseEntity<OnlineUsersDto>(chatRoomTransition.transitAndGetOnlineUsers(), HttpStatus.OK);
    }

}
