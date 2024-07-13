package com.amit.converse.chat.service;

import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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
}