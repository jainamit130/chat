package com.amit.converse.chat.service;

import com.amit.converse.chat.dto.UserResponseDto;
import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.UserRepository;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private final SharedService sharedService;
    private final UserRepository userRepository;

    @KafkaListener(topics = "user-events", groupId = "group_id")
    public User consume(String userEvent) {
        User user = new Gson().fromJson(userEvent, User.class);
        Instant createdAt = Instant.parse(user.getCreationDate());
        user.setLastSeenTimestamp(createdAt);
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + user.getUsername());
        }
        sharedService.createSelfChatRoom(user.getUserId(),user.getUsername());
        return userRepository.save(user);
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

    public void groupJoinedOrLeft(String userId,String chatRoomId,Boolean isJoined){
        User user = getUser(userId);
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

    public List<UserResponseDto> getAllUsers(){
        return userRepository.findAll().stream().map(user -> {
            return UserResponseDto.builder().username(user.getUsername()).id(user.getUserId()).build();
        }).collect(Collectors.toList());
    }

    public List<UserResponseDto> searchUser(String searchPrefix){
        return userRepository.findAllByUsernameStartsWithIgnoreCase(searchPrefix).stream().map(user -> {
            return UserResponseDto.builder().username(user.getUsername()).id(user.getUserId()).build();
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
}