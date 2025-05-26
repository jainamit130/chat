package com.amit.converse.chat.service.User;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.dto.Notification.NewChatNotification;
import com.amit.converse.chat.dto.Notification.UserStatusNotification;
import com.amit.converse.chat.dto.OnlineUsers.GroupChatOnlineUsersDTO;
import com.amit.converse.chat.dto.OnlineUsers.IOnlineUsersDTO;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.Enums.ConnectionStatus;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.chatRoom.ChatService;
import com.amit.converse.chat.service.Notification.UserNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserChatService<T extends ChatRoom> {
    @Autowired
    private UserService userService;
    @Autowired
    protected ChatService<T> chatService;
    @Autowired
    private UserNotificationService userNotificationService;

    private void sendNewChatNotificationToUser(String userId, ChatRoom newChatRoom) {
        userNotificationService.sendNotification(userId,new NewChatNotification(newChatRoom));
    }

    private void disconnectChat(User user, IChatRoom chatRoom) {
        chatRoom.deleteChat(user.getUserId());
        user.deleteChat(chatRoom.getId());
    }

    public IOnlineUsersDTO getOnlineUsersDTO(List<String> onlineUserIdsOfChat) {
        List<User> onlineUsers = getUsersFromRepo(onlineUserIdsOfChat);
        return GroupChatOnlineUsersDTO.builder().onlineUsers(processUsersToUsernames(onlineUsers)).build();
    }

    public IOnlineUsersDTO getOnlineUsersOfChat(){
        return getOnlineUsersDTO(chatService.getOnlineUserIdsOfChat());
    }

    public Integer getUnreadMessageCount(IChatRoom chatRoom) {
        return chatRoom.getUnreadMessageCount(UserService.getUserContext().getUserId());
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

    public void connectbashChat(List<String> userIds,ChatRoom chatRoom) {
        List<User> users = new ArrayList<>(getUsersFromRepo(userIds));
        for(User user:users) {
            connectChat(user,chatRoom);
            sendNewChatNotificationToUser(user.getUserId(),chatRoom);
        }
        processUsersToDB(users);
        chatService.processChatRoomToDB((T) chatRoom);
    }

    public void connectChat(User user,IChatRoom chatRoom) {
        chatRoom.connectChat(user.getUserId());
        user.connectChat(chatRoom.getId());
        chatService.processChatRoomToDB((T) chatRoom);
    }

    public void connectChatAndNotify(User user,ChatRoom chatRoom) {
        connectChat(user,chatRoom);
        sendNewChatNotificationToUser(user.getUserId(),chatRoom);
    }

    // Notify All ChatRooms of a user about status: went online or went offline
    public void notifyStatus(ConnectionStatus status) {
        User user = userService.getUserContext();
        UserStatusNotification userStatusNotification = UserStatusNotification.builder().status(status).username(user.getDisplayName()).build();
        userNotificationService.sendNotificationToUserChats(user, userStatusNotification);
        return;
    }

    public List<ChatRoom> getChatRoomsOfUser() {
        User user = userService.getUserContext();
        // Redis clears the state of any active chatRoom of the user
        userService.clearRedisChatRoomOfUser();
        List<ChatRoom> chatRooms = chatService.getChatRoomsByIds(new ArrayList<>(user.getChatRoomIds()),user.getUserId());

        chatRooms.sort((chatRoom1, chatRoom2) -> {
            return chatRoom2.getLatestMessage().getTimestamp()
                    .compareTo(chatRoom1.getLatestMessage().getTimestamp());
        });

        return chatRooms;
    }

    public User createUser(User user) {
        userService.createUser(user);
        return UserService.getUserContext();
    }
}
