package com.amit.converse.chat.controller;

import com.amit.converse.chat.dto.UserEventDTO;
import com.amit.converse.chat.dto.UserDetails;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/converse/users/")
public class UserController {
//    private final UserServiceClient userServiceClient;
    private final UserService userService;

    @GetMapping("/search")
    public ResponseEntity<List<UserDetails>> searchUser(@RequestParam(name = "q") String searchQuery){
        return new ResponseEntity(userService.searchUser(searchQuery), HttpStatus.OK);
    }

    @GetMapping("/getUser/{userId}")
    public ResponseEntity<UserDetails> getUserDetails(@PathVariable String userId, @RequestParam String loggedInUserId) {
        return new ResponseEntity<UserDetails>(userService.getUserDetails(userId, loggedInUserId), HttpStatus.OK);
    }


    @GetMapping("/getUsers")
    public ResponseEntity<List<UserDetails>> getUsers(){
        return new ResponseEntity(userService.getAllUsers(), HttpStatus.OK);
    }

    @PostMapping("/newUser")
    public ResponseEntity<Void> syncUsers(@RequestBody UserEventDTO userEventDTO) {
        UserEventDTO userEvent = UserEventDTO.builder()
                .userId(userEventDTO.getUserId())
                .username(userEventDTO.getUsername())
                .creationDate(userEventDTO.getCreationDate())
                .build();

        boolean success = userService.consume(userEvent);

        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR) // Return 500 Internal Server Error if failed
                    .body(null);
        }
    }

}
