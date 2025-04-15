package com.amit.converse.chat.config;

import com.amit.converse.chat.context.User.OfflineSetUserContextService;
import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.IUserRepository;
import com.amit.converse.chat.service.User.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    @Lazy
    private UserService userService;
    private final OfflineSetUserContextService setUserContextService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new UnsupportedOperationException("This method is not supported!");
    }

    public UserDetailsImpl loadUserByUserId(String userId) throws UsernameNotFoundException {
        User loadedUser = userService.getUserById(userId);
        setUserContextService.setUser(loadedUser);
        return new UserDetailsImpl(
                loadedUser.getUserId(),
                loadedUser.getPassword()
        );
    }

    public void clearContext() {
        User user = userService.getUserContext();
        if(user!=null) {
            userService.processUserToDB(user);
            setUserContextService.clearContext();
        } else {
            System.out.println("Broo!!");
        }
    }

}
