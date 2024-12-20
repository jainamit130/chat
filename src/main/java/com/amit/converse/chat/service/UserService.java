package com.amit.converse.chat.service;

import com.amit.converse.chat.dto.UserEventDTO;
import com.amit.converse.chat.dto.UserDetails;
import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.model.ChatRoom;
import com.amit.converse.chat.model.ConnectionStatus;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.UserRepository;
import lombok.AllArgsConstructor;
//import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private final SharedService sharedService;
    private final RedisService redisService;
    private final UserRepository userRepository;

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

    public boolean consume(UserEventDTO userEvent) {
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

    public void updateUserLastSeen(String userId, Instant timestamp) {
        User user = getUser(userId);
        user.setLastSeenTimestamp(timestamp);
        saveUser(user);
        return;
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User getUser(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ConverseException("User not found!!"));
        return user;
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

    public void updateLastSeenOfUser(String userId,Instant timestamp){
        User user = getUser(userId);
        user.setLastSeenTimestamp(timestamp);
        userRepository.save(user);
        return;
    }

    public List<UserDetails> getAllUsers(){
        return userRepository.findAll().stream().map(user -> {
            return UserDetails.builder().username(user.getUsername()).id(user.getUserId()).build();
        }).collect(Collectors.toList());
    }

    public List<UserDetails> searchUser(String searchPrefix){
        return userRepository.findAllByUsernameStartsWithIgnoreCase(searchPrefix).stream().map(user -> {
            return UserDetails.builder().username(user.getUsername()).id(user.getUserId()).build();
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
                userDetails.add(UserDetails.builder().username(user.get().getUsername()).id(userId).build());
            }
        }
        return userDetails;
    }

    public List<UserEventDTO> processIdsToUserDetails(List<String> userIds) {
        List<UserEventDTO> userList = new ArrayList<>();

        for (String userId : userIds) {
            Optional<User> user = userRepository.findByUserId(userId);
            if (user.isPresent()) {
                UserEventDTO userEventDTO = UserEventDTO.builder().userId(userId).username(user.get().getUsername()).build();
                userList.add(userEventDTO);
            }
        }
        return userList;
    }

    public UserDetails getUserDetails(String userId, String loggedInUserId) {
        User user = getUser(userId);
        User loggedInUser = getUser(loggedInUserId);
        Set<String> commonChatRoomIdsSet = sharedService.getCommonChatRooms(user.getChatRoomIds(),loggedInUser.getChatRoomIds());
        ConnectionStatus status = redisService.isUserOnline(userId)? ConnectionStatus.ONLINE: ConnectionStatus.OFFLINE;
        UserDetails userDetails = UserDetails.builder().username(user.getUsername()).id(userId).userStatus(user.getStatus()).lastSeenTimestamp(user.getLastSeenTimestamp()).status(status).build();
        Optional<ChatRoom> individualChatRoom;
        if(!userId.equals(loggedInUserId)) {
            individualChatRoom = sharedService.getIndividualChatIfPresent(userId,loggedInUserId);
        } else {
            individualChatRoom = Optional.ofNullable(sharedService.getSelfChatRoom(loggedInUserId));
        }
        if(individualChatRoom.isPresent()){
            userDetails.setCommonIndividualChatId(individualChatRoom.get().getId());
            commonChatRoomIdsSet.remove(individualChatRoom.get().getId());
        }
        userDetails.setCommonChatRoomIds(new ArrayList(commonChatRoomIdsSet));
        return userDetails;
    }


}