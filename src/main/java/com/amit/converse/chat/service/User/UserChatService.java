package com.amit.converse.chat.service.User;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.config.util.SecurityContextUtil;
import com.amit.converse.chat.context.ChatRoom.ChatContext;
import com.amit.converse.chat.dto.Notification.IUserNotification;
import com.amit.converse.chat.dto.Notification.UserChatNotification;
import com.amit.converse.chat.dto.Notification.UserStatusNotification;
import com.amit.converse.chat.dto.OnlineUsers.GroupChatOnlineUsersDTO;
import com.amit.converse.chat.dto.OnlineUsers.IOnlineUsersDTO;
import com.amit.converse.chat.dto.UserDetails;
import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import com.amit.converse.chat.model.Enums.ConnectionStatus;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.chatRoom.ChatService;
import com.amit.converse.chat.service.Notification.UserNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Primary
public class UserChatService<T extends ChatRoom> {
    @Autowired
    private UserService userService;
    @Autowired
    protected ChatService<T> chatService;
    @Autowired
    private UserNotificationService userNotificationService;

    public void sendNotificationToUser(User user, ChatRoom chatRoom, UserChatNotification notification) {
        notification.populateOnlineUsersDTOInTransactionNotification(getOnlineUsersOfChat(chatRoom));
        userNotificationService.sendNotification(user.getUserId(),notification);
    }

    public IOnlineUsersDTO getOnlineUsersDTO(List<String> onlineUserIdsOfChat) {
        List<User> onlineUsers = getUsersFromRepo(onlineUserIdsOfChat);
        return GroupChatOnlineUsersDTO.builder().onlineUsers(processUsersToUsernames(onlineUsers)).build();
    }

    public IOnlineUsersDTO getOnlineUsersOfChat(ChatRoom chatRoom){
        return getOnlineUsersDTO(chatService.getOnlineUserIdsOfChat(chatRoom));
    }

    public IOnlineUsersDTO getOnlineUsersOfChat(){
        return getOnlineUsersDTO(chatService.getOnlineUserIdsOfChat(ChatContext.getChatRoom()));
    }

    public Integer getUnreadMessageCount(IChatRoom chatRoom,User user) {
        if(user.isExited(chatRoom.getId())) return user.getUnreadMessageCountOfExitedChat(chatRoom.getId());
        return chatRoom.getUnreadMessageCount(user.getUserId());
    }

    public void processUsersToDB(List<User> users) {
        userService.processUsersToDB(users);
    }

    public void processUsersAndChatRoomToDB(List<User> users,T chatRoom) {
        processUsersToDB(users);
        chatService.processChatRoomToDB(chatRoom);
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

    public void deleteChat(ChatRoom chatRoom, User user) {
        chatRoom.deleteChat(user.getUserId());
        user.deleteChat(chatRoom);
        processUsersAndChatRoomToDB(Collections.singletonList(user), (T) chatRoom);
    }

    public void deleteChat(ChatRoom chatRoom) {
        List<String> allUserIds = new ArrayList<>(chatRoom.getUserIds());
        allUserIds.addAll(chatRoom.getDeletedForUsers());
        List<User> users = getUsersFromRepo(allUserIds);
        users.stream().forEach((user)->user.deleteChat(chatRoom));
        processUsersToDB(users);
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
        List<String> allChatRoomIds = new ArrayList<>();
        allChatRoomIds.addAll(user.getChatRoomIds());
        allChatRoomIds.addAll(user.getExitedChatRoomIds().keySet());

        List<ChatRoom> chatRooms = chatService.getChatRoomsByIds(allChatRoomIds, user);

        chatRooms.sort((chatRoom1, chatRoom2) -> {
            return chatRoom2.getLatestMessage().getTimestamp()
                    .compareTo(chatRoom1.getLatestMessage().getTimestamp());
        });

        return chatRooms;
    }

    public User createUser(User user) {
        return SecurityContextUtil.populateUserContext(userService.createUser(user));
    }

    public Map<String, Set<UserDetails>> convertMapIdsToMapUserDetails(Map<String, Set<String>> receiptIdsByTime) {
        Map<String, Set<UserDetails>> updatedMap = new HashMap<>();

        for (Map.Entry<String, Set<String>> entry : receiptIdsByTime.entrySet()) {
            String timestamp = entry.getKey();
            Set<UserDetails> userDetails = userService.processIdsToUserDetails(entry.getValue());
            updatedMap.put(timestamp, userDetails);
        }
        return updatedMap;
    }

    public void readMessages(ChatRoom chatRoom,User user) {
        chatRoom.readMessages(user.getUserId());
        if(user.isExited(chatRoom.getId())) {
            user.readExitedChat(chatRoom.getId());
        }
        processUsersAndChatRoomToDB(Collections.singletonList(user), (T) chatRoom);
    }

    public List<UserDetails> getAllUserDetails(String chatRoomId) {
        List<UserDetails> usersDetails = userService.getAllUserDetails();

        if (chatRoomId != null) {
            ChatRoom chatRoom = chatService.getChatRoomById(chatRoomId);
            List<String> groupMembers = chatRoom.getUserIds();

            return usersDetails.stream()
                    .filter(userDetail -> !groupMembers.contains(userDetail.getUserId()))
                    .collect(Collectors.toList());
        }

        return usersDetails;
    }

}
