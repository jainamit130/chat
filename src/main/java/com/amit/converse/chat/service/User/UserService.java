package com.amit.converse.chat.service.User;

import com.amit.converse.chat.context.User.UserContext;
import com.amit.converse.chat.dto.UserDetails;
import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.IUserRepository;
import com.amit.converse.chat.service.AuthService;
import com.amit.converse.chat.service.Redis.Interface.IRedisWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private IRedisWriteService redisWriteService;

    void clearRedisChatRoomOfUser() {
        redisWriteService.removeUserFromChatRoom(getUserContext());
    }

    private void updateContext(User user) {
        UserContext.updateContext(user);
    }

    public User getUserContext() {
        return UserContext.getUser();
    }

    private Optional<User> getContextUserIfPresentInUsers(List<User> users) {
        Optional<User> matchingUser = users.stream()
                .filter(user -> user.getUserId().equals(UserContext.getUserId()))
                .findFirst();
        return matchingUser;
    }

    private List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getLoggedInUser() {
        return getUserById(authService.getLoggedInUserId());
    }

    public User getUserFromRepo(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ConverseException("User not found!"));
        return user;
    }

    public List<User> getUsersFromRepo(List<String> userIds) {
        List<User> users = userRepository.findAllByUserIdIn(userIds);
        return users;
    }

    public void processUserToDB(User user) {
        updateContext(userRepository.save(user));
    }

    public void processUsersToDB(List<User> users) {
        userRepository.saveAll(users);
        Optional<User> getContextUserIfPresent = getContextUserIfPresentInUsers(users);
        if(getContextUserIfPresent.isPresent())
            updateContext(getContextUserIfPresent.get());
    }

    public void transit(User user) {
        user.transitSession();
        processUserToDB(user);
    }

    public User getUserById(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ConverseException("User not found!"));
        return user;
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
        if(UserContext.getUserId().equals(userId)) return userDetailsService.getProfileDetails(UserContext.getUser());
        return userDetailsService.getUserDetails(getUserById(userId));
    }

    public void createUser(User user) throws ConverseException {
        String username = user.getUsername();
        if(userRepository.existsByUsername(username)) {
            throw new ConverseException("Username already exists: " + username);
        }
        processUserToDB(user);
    }
}
