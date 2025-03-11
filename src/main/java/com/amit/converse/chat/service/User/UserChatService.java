package com.amit.converse.chat.service.User;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.dto.Notification.NewChatNotification;
import com.amit.converse.chat.dto.Notification.UserOnlineStatusNotification;
import com.amit.converse.chat.dto.OnlineUsers.GroupChatOnlineUsersDTO;
import com.amit.converse.chat.dto.OnlineUsers.IOnlineUsersDTO;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.Enums.ConnectionStatus;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.ChatRoom.ChatService;
import com.amit.converse.chat.service.Notification.UserNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserChatService<T extends ChatRoom> {
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
    public ChatRoom getContextChatRoom() {
        return chatService.getContextChatRoom();
    }

    public IOnlineUsersDTO getOnlineUsersDTO(List<String> onlineUserIdsOfChat) {
        List<User> onlineUsers = getUsersFromRepo(onlineUserIdsOfChat);
        return GroupChatOnlineUsersDTO.builder().onlineUsers(processUsersToUsernames(onlineUsers)).build();
    }

    public IOnlineUsersDTO getOnlineUsersOfChat(){
        return getOnlineUsersDTO(chatService.getOnlineUserIdsOfChat());
    }

    public Integer getUnreadMessageCount(IChatRoom chatRoom) {
        return chatRoom.getUnreadMessageCount(getContextUser().getUserId());
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

    public User getUserFromRepo(String userId) {
        return userService.getUserFromRepo(userId);
    }

    public List<User> getUsersFromRepo(List<String> userIds) {
        return userService.getUsersFromRepo(userIds);
    }

    public void deleteChat(T chatRoom) {
        User contextUser = userService.getUserContext();
        disconnectChat(contextUser,chatRoom);
        processChatRoomToDB(chatRoom);
        processUsersToDB(Collections.singletonList(contextUser));
    }

    public void connectChat(List<String> userIds,IChatRoom chatRoom) {
        List<User> users = getUsersFromRepo(userIds);
        for(User user:users) {
            connectChat(user,chatRoom);
            sendNewChatNotificationToUser(user.getUserId(),chatRoom);
        }
        processUsersToDB(users);
    }

    public void connectChat(User user,IChatRoom chatRoom) {
        chatRoom.connectChat(user.getUserId());
        user.connectChat(chatRoom.getId());
    }

    public void connectChatAndNotify(User user,IChatRoom chatRoom) {
        connectChat(user,chatRoom);
        sendNewChatNotificationToUser(user.getUserId(),chatRoom);
    }

    // Notify All ChatRooms of a user about status: went online or went offline
    public void notifyStatus(ConnectionStatus status) {
        User user = userService.getUserContext();
        UserOnlineStatusNotification userOnlineStatusNotification = UserOnlineStatusNotification.builder().status(status).username(user.getUsername()).build();
        userNotificationService.sendNotificationToUserChats(user, userOnlineStatusNotification);
        return;
    }

    public List<ChatRoom> getChatRoomsOfUser() {
        User user = userService.getUserContext();
        // Redis clears the state of any active chatRoom of the user
        userService.clearRedisChatRoomOfUser();
        return chatService.getChatRoomsByIds(new ArrayList<>(user.getChatRoomIds()),user.getUserId());
    }

    public User createUser(User user) {
        userService.createUser(user);
        return getContextUser();
    }
}
