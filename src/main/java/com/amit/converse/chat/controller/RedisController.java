package com.amit.converse.chat.controller;

import com.amit.converse.chat.service.RedisService;
import com.amit.converse.chat.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class RedisController {

    private final RedisService redisService;
    private final UserService userService;

    @PostMapping("/save/{key}")
    public String saveData(@PathVariable String key, @RequestParam String value) {
        redisService.saveData(key, value);
        return "Data saved";
    }

    @GetMapping("/get/{key}")
    public Instant getData(@PathVariable String key) {
        return redisService.getData(key);
    }

    @PostMapping("/remove/{key}")
    public String removeData(@PathVariable String key) {
        userService.updateUserLastSeen(key,redisService.getData(key));
        redisService.removeData(key);
        return "User marked as offline";
    }
}
