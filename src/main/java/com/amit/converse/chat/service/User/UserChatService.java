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
    private ChatService chatService;
    @Autowired
    private UserNotificationService userNotificationService;
    @Autowired
    protected ChatContext<T> chatContext;

    private void sendNewChatNotificationToUser(String userId) {
        userNotificationService.sendNotification(userId,new NewChatNotification(chatContext.getChatRoom()));
    }

    private void disconnectChat(User user) {
        IChatRoom chatRoom = chatContext.getChatRoom();
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

    public void deleteChat() {
        User contextUser = userService.getUserContext();
        disconnectChat(contextUser);
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
}
