package com.amit.converse.chat.controller;

import com.amit.converse.chat.dto.UserResponseDto;
import com.amit.converse.chat.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/converse/users/")
public class UserController {
    private final UserService userService;

    @GetMapping("/search")
    public ResponseEntity<List<UserResponseDto>> searchUser(@RequestParam(name = "q") String searchQuery){
        return new ResponseEntity(userService.searchUser(searchQuery), HttpStatus.OK);
    }

    @GetMapping("/getUsers")
    public ResponseEntity<List<UserResponseDto>> getUsers(){
        return new ResponseEntity(userService.getAllUsers(), HttpStatus.OK);
    }
}
