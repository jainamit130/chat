package com.amit.converse.chat.service;

import com.amit.converse.chat.dto.UserResponseDto;
import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.UserRepository;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RedisService redisService;

    @KafkaListener(topics = "user-events", groupId = "group_id")
    public User consume(String userEvent) {

        User user = new Gson().fromJson(userEvent, User.class);
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + user.getUsername());
        }

        return userRepository.save(user);
    }

    public void updateUserLastSeen(String userId, Instant timestamp) {
        User user = getUser(userId);
//        user.setLastSeenTimestamp(timestamp);
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
}