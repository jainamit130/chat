package com.amit.converse.chat.service;

import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.model.ChatRoom;
import com.amit.converse.chat.model.ChatRoomType;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.ChatMessageRepository;
import com.amit.converse.chat.repository.ChatRoomRepository;
import com.amit.converse.chat.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@AllArgsConstructor
public class SharedService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public Optional<ChatRoom> getIndividualChatIfPresent(String userId1,String userId2) {
        Optional<ChatRoom> chatRoom = chatRoomRepository.findIndividualChatRoomByUserIds(ChatRoomType.INDIVIDUAL,userId1,userId2);
        return chatRoom;
    }

    public int getUnreadMessageCountForExitedMembers(ChatRoom chatRoom, String userId) {
        Instant exitedTimeStamp = chatRoom.getExitedMembers().get(userId);
        Integer totalMessageCountForUser = chatMessageRepository.countMessagesOfChatRoomIdToTimestamp(chatRoom.getId(),exitedTimeStamp);
        return totalMessageCountForUser - chatRoom.getReadMessageCount(userId);
    }

    public ChatRoom getSelfChatRoom(String userId) {
        return chatRoomRepository.findSelfChatRoomByUserIds(userId);
    }

    public Set<String> getCommonChatRooms(Set<String> chatRoomIds1,Set<String> chatRoomIds2) {
        // Common Ids
        chatRoomIds1.retainAll(chatRoomIds2);
        return chatRoomIds1;
    }

    public ChatRoom getChatRoom(String chatRoomId){
        if(chatRoomId==null)
            System.out.println("The chatRoom is null!");
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ConverseException("Chat room not found"));
        return chatRoom;
    }

    public List<User> getAllUsers(List<String> userIds) {
        List<User> users = userRepository.findAllByUserIdIn(userIds);
        return users;
    }

    public User getUser(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ConverseException("User not found!!"));
        return user;
    }

    public List<ChatMessage> getMessagesOfChatRoom(String chatRoomId,Instant toTimestamp,String userId, Instant userFetchStartTimestamp, Integer startIndex, Integer pageSize) {
        long totalMessages = chatMessageRepository.countByChatRoomIdAndNotDeletedForUser(chatRoomId,userFetchStartTimestamp,toTimestamp,userId);
        int offset = Math.min(startIndex, (int) totalMessages);
        int remainingMessages = (int) totalMessages - offset;

        if (remainingMessages <= 0) {
            return Collections.emptyList();
        }

        if (pageSize == null || pageSize <= 0) {
            pageSize = remainingMessages;
        }

        Pageable pageable;
        if (pageSize > 0) {
            int pageNumber = offset / pageSize;
            pageable = PageRequest.of(pageNumber, pageSize, Sort.by("timestamp").ascending());
        } else {
            return Collections.emptyList();
        }
        return chatMessageRepository.findMessagesWithPaginationAfterTimestamp(chatRoomId,userFetchStartTimestamp,toTimestamp,userId, pageable);
    }

    public ChatMessage getLatestMessageOfGroup(String chatRoomId,Instant toTimestamp,Instant userFetchStartTimestamp,String userId){
        ChatMessage latestMessage = chatMessageRepository.findLatestMessage(chatRoomId,userFetchStartTimestamp,toTimestamp,userId);
        return latestMessage;
    }

    public User createUserAndSelfChatRoom(User user){
        Map<String, Integer> readMessageCounts = new HashMap<>();
        Map<String, Integer> deliverMessageCounts = new HashMap<>();
        List<String> memberIds = new ArrayList<>();
        memberIds.add(user.getUserId());

        ChatRoom chatRoom = ChatRoom.builder()
                .name(user.getUsername())
                .userIds(memberIds)
                .chatRoomType(ChatRoomType.SELF)
                .readMessageCounts(readMessageCounts)
                .deliveredMessageCounts(deliverMessageCounts)
                .createdBy(user.getUserId())
                .totalMessagesCount(0)
                .createdAt(Instant.now())
                .build();

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        user.addChatRoom(savedChatRoom.getId());
        return userRepository.save(user);
    }

    User getCounterpartUser(ChatRoom chatRoom,String participantUserId) {
        String recipientUserId = chatRoom.getUserIds()
                .stream()
                .filter(userId -> !userId.equals(participantUserId))
                .findFirst()
                .orElseThrow(() -> new ConverseException("No other user found"));
        User recipientUser = userRepository.findByUserId(recipientUserId)
                .orElseThrow(()-> new ConverseException("User not found!"));
        return recipientUser;
    }

    @Async
    public void deleteMessagesFromToInstant(Instant lastClearedTimestamp, String userId, Integer groupSize) {
        List<ChatMessage> messages = chatMessageRepository.findByTimestampBetween(lastClearedTimestamp, Instant.now());
        List<ChatMessage> messagesToSave = new ArrayList<>();

        for (ChatMessage message : messages) {
            message.addUserToDeletedForUsers(userId);
            if (message.getDeletedForUsers().size() == groupSize) {
                chatMessageRepository.deleteById(message.getId());
            } else {
                messagesToSave.add(message);
            }
        }

        if (!messagesToSave.isEmpty()) {
            chatMessageRepository.saveAll(messagesToSave);
        }
    }
}
