package com.amit.converse.chat.service;

import com.amit.converse.chat.State.Offline;
import com.amit.converse.chat.State.Online;
import com.amit.converse.chat.State.State;
import com.amit.converse.chat.context.ChatContext;
import com.amit.converse.chat.context.UserContext;
import com.amit.converse.chat.dto.UserDTO;
import com.amit.converse.chat.dto.UserDetails;
import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.model.ChatRoom;
import com.amit.converse.chat.model.Enums.ConnectionStatus;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.UserRepository;
import com.amit.converse.chat.service.Notification.UserNotificationService;
import com.amit.converse.chat.service.Redis.RedisReadService;
import lombok.AllArgsConstructor;
//import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceOld {

    @Autowired
    private ChatContext chatContext;
    @Autowired
    private UserContext userContext;
    @Autowired
    private AuthService authService;
    @Autowired
    private SharedService sharedService;
    @Autowired
    private RedisReadService redisService;
    @Autowired
    private UserNotificationService userNotificationService;
    @Autowired
    private UserRepository userRepository;

    // consume User
    // joinGroup
    // exitGroup

    public User getLoggedInUser() {
        String userId = authService.getLoggedInUserId();
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ConverseException("User with userId : "+ userId + " not found!"));
        return user;
    }

    public List<User> getUsersFromRepo(List<String> userIds) {
        List<User> users = userRepository.findAllByUserIdIn(userIds);
        return users;
    }



//    @KafkaListener(topics = "user-events", groupId = "group_id")
//    public User consume(String userEvent) {
//        User user = new Gson().fromJson(userEvent, User.class);
//        Instant createdAt = Instant.parse(user.getCreationDate());
//        user.setLastSeenTimestamp(createdAt);
//        if (userRepository.existsByUsername(user.getUsername())) {
//            throw new IllegalArgumentException("Username already exists: " + user.getUsername());
//        }
//        return sharedService.createUserAndSelfChatRoom(user);
//    }

    public boolean consume(UserDTO userEvent) {
        User user = User.builder()
                .userId(userEvent.getUserId())
                .username(userEvent.getUsername())
                .creationDate(userEvent.getCreationDate())
                .lastSeenTimestamp(userEvent.getCreationDate())
                .chatRoomIds(new HashSet<>())
                .build();

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + user.getUsername());
        }

        try {
            sharedService.createUserAndSelfChatRoom(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void updateUserLastSeen(String userId) {
        User user = getUser(userId);
        user.updateLastSeenToNow();
        saveUser(user);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User getUser(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ConverseException("User not found!!"));
        user.setState(getStateFromRedis(user));
        user.updateLastSeenToNow();
        return user;
    }

    State getStateFromRedis(User user) {
        if(redisService.isUserOnline(user.getUserId())){
            return new Online();
        }
        return new Offline();
    }

    public void deleteChat(String userId, String chatRoomId) {
        groupJoinedOrLeft(userId,chatRoomId,false);
    }

    public void groupJoinedOrLeft(String userId,String chatRoomId,Boolean isJoined) {
        User user = getUser(userId);
        groupJoinedOrLeft(user,chatRoomId,isJoined);
    }

    public void groupJoinedOrLeft(User user,String chatRoomId,Boolean isJoined){
        if(isJoined){
            user.addChatRoom(chatRoomId);
        } else {
            user.removeChatRoom(chatRoomId);
        }
        saveUser(user);
    }

    public List<UserDetails> getAllUsers(){
        return userRepository.findAll().stream().map(user -> {
            return UserDetails.builder().username(user.getUsername()).userId(user.getUserId()).build();
        }).collect(Collectors.toList());
    }

    public List<UserDetails> searchUser(String searchPrefix){
        return userRepository.findAllByUsernameStartsWithIgnoreCase(searchPrefix).stream().map(user -> {
            return UserDetails.builder().username(user.getUsername()).userId(user.getUserId()).build();
        }).collect(Collectors.toList());
    }

    public Set<String> processIdsToName(Set<String> userIds) {
        Set<String> usernames = new HashSet<>();

        for (String userId : userIds) {
            Optional<User> user = userRepository.findByUserId(userId);
            if (user.isPresent()) {
                usernames.add(user.get().getUsername());
            }
        }
        return usernames;
    }

    public Set<UserDetails> processIdsToUserDetails(Set<String> userIds) {
        Set<UserDetails> userDetails = new HashSet<>();

        for (String userId : userIds) {
            Optional<User> user = userRepository.findByUserId(userId);
            if (user.isPresent()) {
                userDetails.add(UserDetails.builder().username(user.get().getUsername()).userId(userId).build());
            }
        }
        return userDetails;
    }

    public List<UserDTO> processIdsToUserDetails(List<String> userIds) {
        List<UserDTO> userList = new ArrayList<>();

        for (String userId : userIds) {
            Optional<User> user = userRepository.findByUserId(userId);
            if (user.isPresent()) {
                UserDTO userDTO = UserDTO.builder().userId(userId).username(user.get().getUsername()).build();
                userList.add(userDTO);
            }
        }
        return userList;
    }

    public UserDetails getUserDetails(String userId, String loggedInUserId) {
        User user = getUser(userId);
        User loggedInUser = getUser(loggedInUserId);
        Set<String> commonChatRoomIdsSet = sharedService.getCommonChatRooms(user.getChatRoomIds(),loggedInUser.getChatRoomIds());
        ConnectionStatus status = redisService.isUserOnline(userId)? ConnectionStatus.ONLINE: ConnectionStatus.OFFLINE;
        UserDetails userDetails = UserDetails.builder().username(user.getUsername()).userId(userId).userStatus(user.getStatus()).lastSeenTimestamp(user.getLastSeenTimestamp()).status(status).build();
        Optional<ChatRoom> individualChatRoom;
        if(!userId.equals(loggedInUserId)) {
            individualChatRoom = sharedService.getIndividualChatIfPresent(userId,loggedInUserId);
        } else {
            individualChatRoom = Optional.ofNullable(sharedService.getSelfChatRoom(loggedInUserId));
        }
        if(individualChatRoom.isPresent()){
            userDetails.setCommonChatId(individualChatRoom.get().getId());
            commonChatRoomIdsSet.remove(individualChatRoom.get().getId());
        }
        userDetails.setCommonGroupChatIds(new ArrayList(commonChatRoomIdsSet));
        return userDetails;
    }
}