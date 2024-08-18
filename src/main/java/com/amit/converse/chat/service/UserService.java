package com.amit.converse.chat.service;

import com.amit.converse.chat.dto.UserResponseDto;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // Method to create a new user
    public User createUser(String username) {
        // Check if a user with the same username or email already exists
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists: " + username);
        }

        // Create a new User entity
        User newUser = User.builder()
                .username(username)
                .build();

        // Save the new user to the database
        return userRepository.save(newUser);
    }

    public List<UserResponseDto> getAllUsers(){
        return userRepository.findAll().stream().map(user -> {
            return UserResponseDto.builder().username(user.getUsername()).id(user.getId()).build();
        }).collect(Collectors.toList());
    }

    public List<UserResponseDto> searchUser(String searchPrefix){
        return userRepository.findAllByUsernameStartsWithIgnoreCase(searchPrefix).stream().map(user -> {
            return UserResponseDto.builder().username(user.getUsername()).id(user.getId()).build();
        }).collect(Collectors.toList());
    }
}