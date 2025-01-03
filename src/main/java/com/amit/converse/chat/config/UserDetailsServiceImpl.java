package com.amit.converse.chat.config;

import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new UnsupportedOperationException("This method is not supported!");
    }

    public UserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
        return userRepository.findByUserId(userId)
                .map(user -> new UserDetailsImpl(
                        user.getUserId(),
                        user.getPassword()
                ))
                .orElseThrow(() -> new ConverseException("User Id : " + userId + " not found!"));
    }
}
