package com.amit.converse.chat.service;

import com.amit.converse.chat.exceptions.ConverseException;
import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.model.ChatRoom;
import com.amit.converse.chat.model.ChatRoomType;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.repository.ChatMessageRepository;
import com.amit.converse.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserService userService;
    private final RedisService redisService;
    private final SharedService sharedService;
    private final WebSocketMessageService webSocketMessageService;

    public ChatRoom getChatRoom(String chatRoomId){
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));
        return chatRoom;
    }

    public void setExtraDetails(User user,ChatRoom chatRoom){
        ChatMessage latestMessage = sharedService.getLatestMessageOfGroup(chatRoom.getId());
        chatRoom.setUnreadMessageCount(chatRoom.getUnreadMessageCount(user.getUserId()));
        if(chatRoom.getChatRoomType().equals(ChatRoomType.INDIVIDUAL)){
            chatRoom.setName(user.getUsername()==chatRoom.getCreatorUsername()?chatRoom.getRecipientUsername():chatRoom.getCreatorUsername());
        }
        chatRoom.setLatestMessage(latestMessage);
    }

    public List<ChatRoom> getChatRoomsOfUser(String userId) {
        User user = userService.getUser(userId);
        Set<String> chatRoomIds = user.getChatRoomIds();
        List<ChatRoom> chatRooms = chatRoomRepository.findAllById(chatRoomIds);
        for (ChatRoom chatRoom : chatRooms) {
            setExtraDetails(user,chatRoom);
        }
        return chatRooms;
    }

    public Set<String> getOnlineUsersOfGroup(String chatRoomId){
        ChatRoom chatRoom = getChatRoom(chatRoomId);
        Set<String> onlineUserIds = getOnlineUsersOfGroup(chatRoom);
        return userService.processIdsToName(onlineUserIds);
    }

    public Set<String> getOnlineUsersOfGroup(ChatRoom chatRoom){
        return redisService.filterOnlineUsers(chatRoom.getUserIds());
    }

    public List<ChatMessage> getMessagesOfChatRoom(String chatRoomId,Integer startIndex){
        List<ChatMessage> chatMessages = sharedService.getMessagesOfChatRoom(chatRoomId,startIndex,null);
        return chatMessages != null ? chatMessages : Collections.emptyList();
    }

    public String createGroup(String groupName, ChatRoomType chatRoomType, String createdById, List<String> memberIds) {
        Map<String, Integer> readMessageCounts = new HashMap<>();
        Map<String, Integer> deliverMessageCounts = new HashMap<>();
        User creatorUser = userService.getUser(createdById);
        memberIds.add(createdById);
        memberIds = new ArrayList<>(new HashSet<>(memberIds));
        ChatRoom chatRoom = ChatRoom.builder()
                .name(groupName)
                .creatorUsername(creatorUser.getUsername())
                .userIds(memberIds)
                .chatRoomType(chatRoomType)
                .readMessageCounts(readMessageCounts)
                .deliveredMessageCounts(deliverMessageCounts)
                .createdBy(createdById)
                .totalMessagesCount(0)
                .createdAt(Instant.now())
                .build();

        if (chatRoomType == ChatRoomType.INDIVIDUAL) {
            Optional<ChatRoom> existingChatRoomOptional = chatRoomRepository.findIndividualChatRoomByUserIds(ChatRoomType.INDIVIDUAL,memberIds.get(0),memberIds.get(1));
            if(existingChatRoomOptional.isPresent()){
                return existingChatRoomOptional.get().getId();
            }
            User recipientUser = sharedService.getRecipientUser(chatRoom);
            chatRoom.setRecipientUsername(recipientUser.getUsername());
        }

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        List<User> users = sharedService.getAllUsers(memberIds);
        if(chatRoomType!=ChatRoomType.INDIVIDUAL){
            for (User user : users) {
                userService.groupJoinedOrLeft(user,savedChatRoom.getId(),true);
                webSocketMessageService.sendNewGroupStatusToMember(user.getUserId(),savedChatRoom);
            }
        }

        return savedChatRoom.getId();
    }

    public void sendNewChatStatusToMember(String chatRoomId){
        ChatRoom chatRoom = getChatRoom(chatRoomId);
        for (String userId : chatRoom.getUserIds()) {
            User user = sharedService.getUser(userId);
            setExtraDetails(user,chatRoom);
            userService.groupJoinedOrLeft(user,chatRoomId,true);
            webSocketMessageService.sendNewGroupStatusToMember(userId,chatRoom);
        }
    }

    public ChatRoom addMembers(String chatRoomId, List<String> memberIds) {

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));
        List<User> userIds = sharedService.getAllUsers(memberIds);
        for(User user : userIds){
            userService.groupJoinedOrLeft(user,chatRoom.getId(),true);
        }
        chatRoom.getUserIds().addAll(memberIds);
        return chatRoomRepository.save(chatRoom);
    }

    public ChatRoom removeMembers(String chatRoomId, List<String> memberIds) {

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));
        List<User> users = sharedService.getAllUsers(memberIds);
        for(User user : users){
            userService.groupJoinedOrLeft(user,chatRoom.getId(),false);
        }
        chatRoom.getUserIds().removeAll(memberIds);
        return chatRoomRepository.save(chatRoom);
    }

    public List<ChatMessage> getMessagesOfChatRoom(String chatRoomId, Pageable pageable) {
        List<ChatMessage> messages =chatMessageRepository.findMessagesWithPagination(chatRoomId,pageable);
        return messages;
    }

    public List<ChatMessage> getMessagesToBeMarked(String chatRoomId, Integer toBeMarkedMessagesCount) {
        PageRequest pageRequest = PageRequest.of(0, toBeMarkedMessagesCount, Sort.by(Sort.Direction.DESC, "timestamp"));
        List<ChatMessage> messagesToBeMarked = getMessagesOfChatRoom(chatRoomId,pageRequest);
        return messagesToBeMarked;
    }

    public void saveChatRoom(ChatRoom chatRoom){
        chatRoomRepository.save(chatRoom);
        return;
    }
}