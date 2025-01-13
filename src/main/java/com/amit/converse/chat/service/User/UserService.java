package com.amit.converse.chat.service.User;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.context.UserContext;
import com.amit.converse.chat.dto.Notification.NewChatNotification;
import com.amit.converse.chat.dto.Notification.UserOnlineNotification;
import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.model.Enums.ConnectionStatus;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.UserRepository;
import com.amit.converse.chat.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    protected UserContext userContext;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    private AuthService authService;

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

    private Optional<User> getContextUserIfPresentInUsers(List<User> users) {
        Optional<User> matchingUser = users.stream()
                .filter(user -> user.getUserId().equals(userContext.getUserId()))
                .findFirst();
        return matchingUser;
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

    private void updateContext(User user) {
        if(user.getUserId().equals(userContext.getUserId())) {
            userContext.setUser(user);
        }
    }

}
