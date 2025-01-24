package com.amit.converse.chat.controller;

import com.amit.converse.chat.dto.UserDTO;
import com.amit.converse.chat.dto.UserDetails;
import com.amit.converse.chat.service.User.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/converse/users/")
public class UserController {
//  private final UserServiceClient userServiceClient;
    private final UserService userService;

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
            userService.createUser(userDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

}
