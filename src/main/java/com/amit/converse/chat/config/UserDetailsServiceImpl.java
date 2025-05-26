package com.amit.converse.chat.config;

import com.amit.converse.chat.State.StateFactoryService;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.User.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserService userService;

    @Autowired
    private StateFactoryService stateFactoryService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new UnsupportedOperationException("This method is not supported!");
    }

    public User loadUserByUserId(String userId) throws UsernameNotFoundException {
        User user = userService.getUserById(userId);
        user.setState(stateFactoryService.getState(user));
        return user;
    }
}
