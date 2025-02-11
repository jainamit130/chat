package com.amit.converse.chat.service.User;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.context.ChatContext;
import com.amit.converse.chat.dto.Notification.NewChatNotification;
import com.amit.converse.chat.dto.Notification.UserOnlineNotification;
import com.amit.converse.chat.dto.OnlineUsers.GroupChatOnlineUsersDto;
import com.amit.converse.chat.dto.OnlineUsers.IOnlineUsersDTO;
import com.amit.converse.chat.model.Enums.ConnectionStatus;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.ChatRoom.ChatService;
import com.amit.converse.chat.service.Notification.UserNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserChatService<T extends IChatRoom> {
    @Autowired
    @Lazy
    private UserService userService;
    @Autowired
    @Lazy
    protected ChatService<T> chatService;
    @Autowired
    private UserNotificationService userNotificationService;

    private void sendNewChatNotificationToUser(String userId, IChatRoom newChatRoom) {
        userNotificationService.sendNotification(userId,new NewChatNotification(newChatRoom));
    }

    private void disconnectChat(User user, IChatRoom chatRoom) {
        chatRoom.deleteChat(user.getUserId());
        user.disconnectChat(chatRoom.getId(), chatRoom.getUnreadMessageCount(user.getUserId()));
    }

    public User getContextUser() {
        return userService.getUserContext();
    }

    public IOnlineUsersDTO getOnlineUsersDTO(List<String> onlineUserIdsOfChat) {
        List<User> onlineUsers = getUsersFromRepo(onlineUserIdsOfChat);
        return GroupChatOnlineUsersDto.builder().onlineUsers(processUsersToUsernames(onlineUsers)).build();
    }

    public IOnlineUsersDTO getOnlineUsersOfChat(){
        return getOnlineUsersDTO(chatService.getOnlineUserIdsOfChat());
    }

    public void processChatRoomToDB(T chatRoom) {
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

    public void deleteChat(IChatRoom chatRoom) {
        User contextUser = userService.getUserContext();
        disconnectChat(contextUser,chatRoom);
    }

    public void connectChat(List<String> userIds,IChatRoom chatRoom) {
        List<User> users = getUsersFromRepo(userIds);
        for(User user:users) {
            connectChat(user,chatRoom);
        }
        processUsersToDB(users);
    }

    public void connectChat(User user,IChatRoom chatRoom) {
        user.connectChat(chatRoom.getId());
        sendNewChatNotificationToUser(user.getUserId(),chatRoom);
    }

    // Notify All ChatRooms of a user about status: went online or went offline
    public void notifyStatus(ConnectionStatus status) {
        User user = userService.getUserContext();
        UserOnlineNotification userOnlineNotification = UserOnlineNotification.builder().status(status).username(user.getUsername()).build();
        userNotificationService.sendNotificationToUserChats(user,userOnlineNotification);
        return;
    }

    public List<IChatRoom> getChatRoomsOfUser() {
        User user = userService.getUserContext();
        return chatService.getChatRoomsByIds(new ArrayList<>(user.getChatRoomIds()));
    }

    public User createUser(User user) {
        userService.createUser(user);
        return getContextUser();
    }
}
