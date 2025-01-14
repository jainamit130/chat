package com.amit.converse.chat.service.User;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.context.ChatContext;
import com.amit.converse.chat.context.UserContext;
import com.amit.converse.chat.dto.Notification.NewChatNotification;
import com.amit.converse.chat.dto.Notification.UserOnlineNotification;
import com.amit.converse.chat.dto.OnlineUsers.IOnlineUsersDTO;
import com.amit.converse.chat.model.Enums.ConnectionStatus;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.ChatRoom.ChatService;
import com.amit.converse.chat.service.Notification.UserNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserChatService<T extends IChatRoom> {
    @Autowired
    private UserService userService;
    @Autowired
    private ChatService chatService;
    @Autowired
    private UserNotificationService userNotificationService;
    @Autowired
    protected UserContext userContext;
    @Autowired
    protected ChatContext<T> chatContext;

    public void processChatRoomToDB(IChatRoom chatRoom) {
        chatService.processChatRoomToDB(chatRoom);
    }

    public void processUsersToDB(List<User> users) {
        userService.processUsersToDB(users);
    }

    public List<String> processUsersToUsernames(List<User> users) {
        return userService.processUsersToUsernames(users);
    }

    public List<User> getUsersFromRepo(List<String> userIds) {
        return userService.getUsersFromRepo(userIds);
    }

    public void disconnectChat() {
        User contextUser = userContext.getUser();
        disconnectChat(contextUser);
    }

    public void disconnectChat(User user) {
        IChatRoom chatRoom = chatContext.getChatRoom();
        user.disconnectChat(chatRoom.getId());
    }

    public void connectChat(List<String> userIds) {
        List<User> users = getUsersFromRepo(userIds);
        for(User user:users) {
            connectChat(user);
        }
        processUsersToDB(users);
    }

    public void connectChat(User user) {
        IChatRoom chatRoom = chatContext.getChatRoom();
        user.connectChat(chatRoom.getId());
        sendNewChatNotificationToUser(user.getUserId());
    }

    private void sendNewChatNotificationToUser(String userId) {
        userNotificationService.sendNotification(userId,new NewChatNotification(chatContext.getChatRoom()));
    }

    // Notify All ChatRooms of a user about status: went online or went offline
    public void notifyStatus(ConnectionStatus status) {
        User user = userContext.getUser();
        UserOnlineNotification userOnlineNotification = UserOnlineNotification.builder().status(status).username(user.getUsername()).build();
        userNotificationService.sendNotificationToUserChats(user,userOnlineNotification);
        return;
    }

    public IOnlineUsersDTO getOnlineUsersOfChat(){
        return getOnlineUsersDTO(chatService.getOnlineUserIdsOfChat());
    }
}
