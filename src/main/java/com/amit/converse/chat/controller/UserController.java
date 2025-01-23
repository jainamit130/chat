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
    public ResponseEntity<Void> syncUsers(@RequestBody UserDTO userDTO) {
        UserDTO userEvent = UserDTO.builder()
                .userId(userDTO.getUserId())
                .username(userDTO.getUsername())
                .creationDate(userDTO.getCreationDate())
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
