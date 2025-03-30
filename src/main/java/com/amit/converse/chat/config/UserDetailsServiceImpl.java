package com.amit.converse.chat.config;

import com.amit.converse.chat.context.User.OfflineSetUserContextService;
import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.IUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final IUserRepository userRepository;
    private final OfflineSetUserContextService setUserContextService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new UnsupportedOperationException("This method is not supported!");
    }

    public UserDetailsImpl loadUserByUserId(String userId) throws UsernameNotFoundException {
        User loadedUser = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ConverseException("User Id : " + userId + " not found!"));

        setUserContextService.setUser(loadedUser);
        return new UserDetailsImpl(
                loadedUser.getUserId(),
                loadedUser.getPassword()
        );
    }

}
