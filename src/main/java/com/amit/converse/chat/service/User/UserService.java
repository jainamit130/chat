package com.amit.converse.chat.service.User;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.Interface.ITransactable;
import com.amit.converse.chat.context.ChatContext;
import com.amit.converse.chat.context.UserContext;
import com.amit.converse.chat.dto.Notification.NewChatNotification;
import com.amit.converse.chat.dto.Notification.UserOnlineNotification;
import com.amit.converse.chat.dto.OnlineStatusDto;
import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.model.Enums.ConnectionStatus;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.UserRepository;
import com.amit.converse.chat.service.AuthService;
import com.amit.converse.chat.service.Notification.UserNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService<T extends IChatRoom> {

    @Autowired
    protected ChatContext<T> chatContext;
    @Autowired
    protected UserContext userContext;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected UserNotificationService userNotificationService;
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
        updateContext(userRepository.save(user));
    }

    public User getUserById(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new ConverseException("User not found!"));
    }

    public void joinChat(String userId) {
        User user = getUserById(userId);
        IChatRoom chatRoom = chatContext.getChatRoom();
        user.joinChatRoom(chatRoom.getId());
        sendUserNotification(userId,chatRoom);
        processUserToDB(user);
    }

    // Notify All ChatRooms of a user about status: went online or went offline
    public void notifyStatus(ConnectionStatus status) {
        User user = userContext.getUser();
        UserOnlineNotification userOnlineNotification = UserOnlineNotification.builder().status(status).username(user.getUsername()).build();
        userNotificationService.sendNotificationToUserChats(user,userOnlineNotification);
        return;
    }

    private void sendUserNotification(String userId,IChatRoom chatRoom) {
        userNotificationService.sendNotification(userId,new NewChatNotification(chatRoom));
    }

    private void updateContext(User user) {
        if(user.getUserId().equals(userContext.getUser().getUserId())) {
            userContext.setUser(user);
        }
    }
}
