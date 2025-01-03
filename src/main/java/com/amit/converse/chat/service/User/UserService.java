package com.amit.converse.chat.service.User;

import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.UserRepository;
import com.amit.converse.chat.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AuthService authService;

    public User getLoggedInUser() {
        return getUserById(authService.getLoggedInUserId());
    }

    private User getUserById(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new ConverseException("User not found!"));
    }
}
