package com.amit.converse.chat.service.User;

import com.amit.converse.chat.config.util.SecurityContextUtil;
import com.amit.converse.chat.dto.UserDetails;
import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.IUserRepository;
import com.amit.converse.chat.service.AuthService;
import com.amit.converse.chat.service.Redis.Interface.IRedisWriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

    public static User getUserContext() {
        return AuthService.getUser();
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

    public User processUserToDB(User user) {
        return SecurityContextUtil.populateUserContext(userRepository.save(user));
    }

    public void processUsersToDB(List<User> users) {
        String contextUserId = getUserContext().getUserId();
        userRepository.saveAll(users).stream().forEach((user) -> {
            if(user.getUserId().equals(contextUserId)) {
                SecurityContextUtil.populateUserContext(user);
            }
        });
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
        return users.stream().map(user -> user.getDisplayName()).collect(Collectors.toList());
    }

    public List<UserDetails> getAllUserDetails() {
        return getAllUsers().stream().map(user -> {
            return UserDetails.builder().username(user.getDisplayName()).userId(user.getUserId()).build();
        }).collect(Collectors.toList());
    }


    public UserDetails getProfileDetails(String userId) {
        if(UserService.getUserContext().getUserId().equals(userId)) return userDetailsService.getProfileDetails(UserService.getUserContext());
        return userDetailsService.getUserDetails(getUserById(userId));
    }

    public User createUser(User user) throws ConverseException {
        String username = user.getDisplayName();
        if(userRepository.existsByUsername(username)) {
            throw new ConverseException("Username already exists: " + username);
        }
        return processUserToDB(user);
    }

    public Set<UserDetails> processIdsToUserDetails(Set<String> userIds) {
        Set<UserDetails> userDetails = new HashSet<>();

        for (String userId : userIds) {
            Optional<User> user = userRepository.findByUserId(userId);
            if (user.isPresent()) userDetails.add(UserDetails.builder().username(user.get().getDisplayName()).userId(userId).build());
        }
        return userDetails;
    }
}
