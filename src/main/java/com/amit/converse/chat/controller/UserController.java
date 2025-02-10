package com.amit.converse.chat.controller;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.dto.UserDTO;
import com.amit.converse.chat.dto.UserDetails;
import com.amit.converse.chat.service.CreateUserService;
import com.amit.converse.chat.service.User.UserChatService;
import com.amit.converse.chat.service.User.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/converse/users/")
public class UserController {
    private final UserService userService;
    private final UserChatService userChatService;
    protected CreateUserService createUserService;

    @QueryMapping
    public List<IChatRoom> getChatRoomsOfUser() {
        List<IChatRoom> chatRooms=userChatService.getChatRoomsOfUser();
        return chatRooms;
    }

    @GetMapping("/get/profile/{userId}")
    public ResponseEntity<UserDetails> getProfileDetails(@RequestParam String userId) {
        return new ResponseEntity<UserDetails>(userService.getProfileDetails(userId), HttpStatus.OK);
    }

    // For now All users are shown to everyone. Meaning Everyone is open to chat with anyone.
    @GetMapping("/getUsers")
    public ResponseEntity<List<UserDetails>> getUsers(){
        return new ResponseEntity(userService.getAllUserDetails(), HttpStatus.OK);
    }

    @PostMapping("/newUser")
    public ResponseEntity syncUsers(@RequestBody UserDTO userDTO) {
        try {
            createUserService.createUser(userDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

}
