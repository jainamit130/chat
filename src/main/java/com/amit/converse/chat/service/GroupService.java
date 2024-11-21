package com.amit.converse.chat.service;

import com.amit.converse.chat.dto.GroupDetails;
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
        if(chatRoomId==null)
            System.out.println("The chatRoom is null!");
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Chat room not found"));
        return chatRoom;
    }

    public Instant getLastSeenTimeStampOfCouterPartUser(ChatRoom chatRoom, String userId) {
        if(chatRoom.getChatRoomType()==ChatRoomType.INDIVIDUAL){
            User counterPartUser = sharedService.getCounterpartUser(chatRoom,userId);
            return counterPartUser.getLastSeenTimestamp();
        }
        return null;
    }

    public void setExtraDetails(User user,ChatRoom chatRoom){
        Instant toTimestamp = Instant.now();
        if(chatRoom.isExitedMember(user.getUserId())){
            toTimestamp=chatRoom.getExitedMembers().get(user.getUserId());
        }
        Instant userFetchStartTimestamp = chatRoom.getUserFetchStartTimeMap().getOrDefault(user.getUserId(), chatRoom.getCreatedAt());
        ChatMessage latestMessage = sharedService.getLatestMessageOfGroup(chatRoom.getId(),toTimestamp,userFetchStartTimestamp,user.getUserId());
        chatRoom.setUnreadMessageCount(chatRoom.getUnreadMessageCount(user.getUserId()));
        if(chatRoom.getChatRoomType().equals(ChatRoomType.INDIVIDUAL)){
            chatRoom.setName(user.getUsername()==chatRoom.getCreatorUsername()?chatRoom.getRecipientUsername():chatRoom.getCreatorUsername());
        }
        chatRoom.setIsExited(chatRoom.isExitedMember(user.getUserId()));
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

    public Set<String> getOnlineUsersOfGroup(ChatRoom chatRoom){
        Set<String> onlineUserIds = redisService.filterOnlineUsers(chatRoom.getUserIds());
        return userService.processIdsToName(onlineUserIds);
    }

    public List<ChatMessage> getMessagesOfChatRoom(String chatRoomId, String userId,Integer startIndex){
        ChatRoom chatRoom = getChatRoom(chatRoomId);
        Instant toTimestamp = Instant.now();
        if(chatRoom.isExitedMember(userId)){
            toTimestamp=chatRoom.getExitedMembers().get(userId);
        }
        Instant userFetchStartTimestamp = chatRoom.getUserFetchStartTimeMap().getOrDefault(userId, chatRoom.getCreatedAt());
        List<ChatMessage> chatMessages = sharedService.getMessagesOfChatRoom(chatRoom.getId(),toTimestamp,userId,userFetchStartTimestamp,startIndex,null);
        return chatMessages != null ? chatMessages : Collections.emptyList();
    }

    public Boolean clearChat(String chatRoomId, String userId) {
        ChatRoom chatRoom = getChatRoom(chatRoomId);
        return clearChat(chatRoom,userId);
    }

    public Boolean clearChat(ChatRoom chatRoom, String userId) {
        try {
            Map<String, Instant> userFetchStartTimeMap = chatRoom.getUserFetchStartTimeMap();
            Instant lastClearedTimestamp = chatRoom.getCreatedAt();
            if(userFetchStartTimeMap.containsKey(userId)){
                lastClearedTimestamp=userFetchStartTimeMap.get(userId);
            }
            // Delete for this user from the last cleared instant to the current instant
            sharedService.deleteMessagesFromToInstant(lastClearedTimestamp,userId,chatRoom.getUserIds().size()+chatRoom.getExitedMembers().size());
            userFetchStartTimeMap.put(userId, Instant.now());
            return true;
        } catch (Exception e) {
            System.err.println("Failed to clear chat for chatRoomId: " + chatRoom.getId());
            e.printStackTrace();
            return false;
        }
    }

    public User getCounterPartUser(ChatRoom chatRoom,String userId) {
        return sharedService.getCounterpartUser(chatRoom,userId);
    }


    public Map.Entry<String, Boolean> createGroup(String groupName, ChatRoomType chatRoomType, String createdById, List<String> memberIds) {
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
            User counterpartUser = sharedService.getCounterpartUser(chatRoom,createdById);
            if(existingChatRoomOptional.isPresent()){
                return  new AbstractMap.SimpleEntry(existingChatRoomOptional.get().getId(),true);
            }
            chatRoom.setRecipientUsername(counterpartUser.getUsername());
        }

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        List<User> users = sharedService.getAllUsers(memberIds);
        if(chatRoomType!=ChatRoomType.INDIVIDUAL){
            for (User user : users) {
                userService.groupJoinedOrLeft(user,savedChatRoom.getId(),true);
                webSocketMessageService.sendNewGroupStatusToMember(user.getUserId(),savedChatRoom);
            }
        }

        return  new AbstractMap.SimpleEntry(savedChatRoom.getId(),false);
    }

    public void sendNewChatStatusToDeletedMembers(String chatRoomId){
        ChatRoom chatRoom = getChatRoom(chatRoomId);
        Set<String> deletedForUsers = chatRoom.getDeletedForUsers();
        for (String userId : chatRoom.getUserIds()) {
            User user = sharedService.getUser(userId);
            setExtraDetails(user,chatRoom);
            if(deletedForUsers.contains(userId)) {
                deletedForUsers.remove(userId);
                chatRoom.setUnreadMessageCount(1);
                chatRoom.setDeletedForUsers(deletedForUsers);
                userService.groupJoinedOrLeft(user,chatRoomId,true);
                webSocketMessageService.sendNewGroupStatusToMember(userId,chatRoom);
            }
        }
        chatRoom.setDeletedForUsers(deletedForUsers);
        chatRoomRepository.save(chatRoom);
    }

    public GroupDetails getGroupDetails(String chatRoomId) {
        ChatRoom chatRoom = getChatRoom(chatRoomId);
        GroupDetails groupDetails = GroupDetails.builder().chatRoomId(chatRoomId).members(userService.processIdsToUserDetails(chatRoom.getUserIds())).build();
        return groupDetails;
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

    public Boolean removeMembers(String chatRoomId, List<String> memberIds) {
        ChatRoom chatRoom = getChatRoom(chatRoomId);
        if(chatRoom.getChatRoomType()==ChatRoomType.INDIVIDUAL){
            return false;
        }

        List<User> users = sharedService.getAllUsers(memberIds);

        if (users.size() != memberIds.size()) {
            return false;
        }

        for (User user : users) {
            chatRoom.exitGroup(user.getUserId());
            webSocketMessageService.sendExitMemberStatus(chatRoomId,user.getUserId());
        }

        boolean membersRemoved = chatRoom.getUserIds().removeAll(memberIds);

        if (membersRemoved) {
            chatRoomRepository.save(chatRoom);
            return true;
        } else {
            return false;
        }
    }


    public List<ChatMessage> getMessagesOfChatRoom(String chatRoomId, String userId, Pageable pageable) {
        ChatRoom chatRoom = getChatRoom(chatRoomId);
        Instant toTimestamp = Instant.now();
        if(chatRoom.isExitedMember(userId)){
            toTimestamp=chatRoom.getExitedMembers().get(userId);
        }
        Instant userFetchStartTimestamp = chatRoom.getUserFetchStartTimeMap().getOrDefault(userId, chatRoom.getCreatedAt());
        List<ChatMessage> messages =chatMessageRepository.findMessagesWithPaginationAfterTimestamp(chatRoomId,userFetchStartTimestamp,toTimestamp,userId,pageable);
        return messages;
    }

    public List<ChatMessage> getMessagesToBeMarked(String chatRoomId, String userId, Integer toBeMarkedMessagesCount) {
        PageRequest pageRequest = PageRequest.of(0, toBeMarkedMessagesCount, Sort.by(Sort.Direction.DESC, "timestamp"));
        List<ChatMessage> messagesToBeMarked = getMessagesOfChatRoom(chatRoomId,userId,pageRequest);
        return messagesToBeMarked;
    }

    public void saveChatRoom(ChatRoom chatRoom){
        chatRoomRepository.save(chatRoom);
        return;
    }

    public Boolean deleteChat(String chatRoomId, String userId) {
        ChatRoom chatRoom = getChatRoom(chatRoomId);

        if (chatRoom.getChatRoomType() == ChatRoomType.GROUP) {
            return deleteGroupChat(chatRoom, userId);
        }

        if (chatRoom.getChatRoomType() == ChatRoomType.INDIVIDUAL) {
            return deleteIndividualChat(chatRoom, userId);
        }

        return false;
    }

    private Boolean deleteGroupChat(ChatRoom chatRoom, String userId) {
        if (!chatRoom.isExitedMember(userId)) {
            return false;
        }
        if (chatRoom.deleteChat(userId)) {
            userService.groupJoinedOrLeft(userId,chatRoom.getId(),false);
            clearChat(chatRoom, userId);
            if(chatRoom.getDeletedForUsers().size()==chatRoom.getExitedMembers().size() && chatRoom.getUserIds().isEmpty()) {
                chatRoomRepository.deleteById(chatRoom.getId());
            } else {
                chatRoomRepository.save(chatRoom);
            }
        }
        redisService.removeUserFromChatRoom(chatRoom.getId(),userId);
        return true;
    }

    private Boolean deleteIndividualChat(ChatRoom chatRoom, String userId) {
        if (chatRoom.deleteChat(userId)) {
            userService.groupJoinedOrLeft(userId,chatRoom.getId(),false);
            if(chatRoom.getDeletedForUsers().size()==2) {
                chatRoomRepository.deleteById(chatRoom.getId());
            } else {
                chatRoomRepository.save(chatRoom);
            }
            clearChat(chatRoom, userId);
            redisService.removeUserFromChatRoom(chatRoom.getId(),userId);
            return true;
        }
        return false;
    }
}