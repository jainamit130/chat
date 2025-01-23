package com.amit.converse.chat.service.User;

import com.amit.converse.chat.context.UserContext;
import com.amit.converse.chat.dto.UserDetails;
import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.UserRepository;
import com.amit.converse.chat.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    protected UserContext userContext;
    @Autowired
    protected UserDetailsService userDetailsService;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    private AuthService authService;

    private void updateContext(User user) {
        if(user.getUserId().equals(userContext.getUserId())) {
            userContext.setUser(user);
        }
    }

    private Optional<User> getContextUserIfPresentInUsers(List<User> users) {
        Optional<User> matchingUser = users.stream()
                .filter(user -> user.getUserId().equals(userContext.getUserId()))
                .findFirst();
        return matchingUser;
    }

    private List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getLoggedInUser() {
        return getUserById(authService.getLoggedInUserId());
    }

    public List<User> getUsersFromRepo(List<String> userIds) {
        List<User> users = userRepository.findAllByUserIdIn(userIds);
        return users;
    }

    public void processUserToDB(User user) {
        userRepository.save(user);
        if(user.equals(userContext.getUser()))
            updateContext(userRepository.save(user));
    }

    public void processUsersToDB(List<User> users) {
        userRepository.saveAll(users);
        Optional<User> getContextUserIfPresent = getContextUserIfPresentInUsers(users);
        if(getContextUserIfPresent.isPresent())
            updateContext(getContextUserIfPresent.get());
    }

    public User getUserById(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new ConverseException("User not found!"));
    }

    public List<String> processUsersToUsernames(List<User> users) {
        return users.stream().map(user -> user.getUsername()).collect(Collectors.toList());
    }

    public List<UserDetails> getAllUserDetails() {
        return getAllUsers().stream().map(user -> {
            return UserDetails.builder().username(user.getUsername()).userId(user.getUserId()).build();
        }).collect(Collectors.toList());
    }


    public UserDetails getProfileDetails(String userId) {
        if(userContext.getUserId().equals(userId)) return userDetailsService.getProfileDetails(userContext.getUser());
        return userDetailsService.getUserDetails(getUserById(userId));
    }
}
