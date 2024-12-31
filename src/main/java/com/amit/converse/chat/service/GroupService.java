package com.amit.converse.chat.service;

import com.amit.converse.chat.dto.GroupDetails;
import com.amit.converse.chat.dto.OnlineUsersDto;
import com.amit.converse.chat.model.*;
import com.amit.converse.chat.model.Messages.ChatMessage;
import com.amit.converse.chat.repository.ChatMessageRepository;
import com.amit.converse.chat.repository.ChatRoomRepository;
import com.amit.converse.chat.service.Redis.RedisChatRoomService;
import com.amit.converse.chat.service.Redis.RedisReadService;
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
    private final RedisReadService redisReadService;
    private final RedisChatRoomService redisChatRoomService;
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
        if(chatRoom.getChatRoomType()==ChatRoomType.DIRECT){
            User counterPartUser = sharedService.getCounterpartUser(chatRoom,userId);
            return counterPartUser.getLastSeenTimestamp();
        }
        return null;
    }

    public void setExtraDetails(User user,ChatRoom chatRoom){
        Instant toTimestamp = Instant.now();
        if(chatRoom.isExitedMember(user.getUserId()) ){
            toTimestamp=chatRoom.getExitedMembers().get(user.getUserId());
            ChatMessage lastExitedMessage = chatMessageRepository.getLastExitedMessage(chatRoom.getId(),user.getUserId());
            if(lastExitedMessage!=null && !lastExitedMessage.getReadRecipients().contains(user.getUserId()))
                chatRoom.setUnreadMessageCount(sharedService.getUnreadMessageCountForExitedMembers(chatRoom, user.getUserId()));
            else
                chatRoom.setUnreadMessageCount(chatRoom.getUnreadMessageCount(user.getUserId()));
        } else {
            chatRoom.setUnreadMessageCount(chatRoom.getUnreadMessageCount(user.getUserId()));
        }
        Instant userFetchStartTimestamp = chatRoom.getUserFetchStartTimeMap().getOrDefault(user.getUserId(), chatRoom.getCreatedAt());
        ChatMessage latestMessage = sharedService.getLatestMessageOfGroup(chatRoom.getId(),toTimestamp,userFetchStartTimestamp,user.getUserId());
        if(chatRoom.getChatRoomType().equals(ChatRoomType.DIRECT)){
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
        Set<String> onlineUserIds = redisReadService.filterOnlineUsers(chatRoom.getUserIds());
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
            Instant lastClearedTimestamp = userFetchStartTimeMap.getOrDefault(userId,chatRoom.getCreatedAt());
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

        if (chatRoomType == ChatRoomType.DIRECT) {
            Optional<ChatRoom> existingChatRoomOptional = chatRoomRepository.findIndividualChatRoomByUserIds(ChatRoomType.DIRECT,memberIds.get(0),memberIds.get(1));
            User counterpartUser = sharedService.getCounterpartUser(chatRoom,createdById);
            if(existingChatRoomOptional.isPresent()){
                return  new AbstractMap.SimpleEntry(existingChatRoomOptional.get().getId(),true);
            }
            chatRoom.setRecipientUsername(counterpartUser.getUsername());
        }

        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        List<User> users = sharedService.getAllUsers(memberIds);
        if(chatRoomType!=ChatRoomType.DIRECT){
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

    // Added by User, All Users, ChatRoom, Notification Service
    public Boolean addMembers(String chatRoomId, String addedByUserId, List<String> memberIds) {
        ChatRoom chatRoom = getChatRoom(chatRoomId);
        List<User> users = sharedService.getAllUsers(memberIds);


        if (users.size() != memberIds.size()) {
            return false;
        }

        User addedByUser = sharedService.getUser(addedByUserId);

        Instant timeAdded = Instant.now();
        for(User user : users){
            notifyGroupAboutMember(chatRoom,user,addedByUser,true);
            chatRoom.addToGroup(user.getUserId(),timeAdded);
            userService.groupJoinedOrLeft(user,chatRoom.getId(),true);
            webSocketMessageService.sendNewGroupStatusToMember(user.getUserId(),chatRoom);
        }

        boolean membersAdded = chatRoom.getUserIds().addAll(memberIds);
        if (membersAdded) {
            chatRoomRepository.save(chatRoom);
            return true;
        } else {
            return false;
        }
    }

    public Boolean removeMembers(String chatRoomId, String removedById, List<String> memberIds) {
        ChatRoom chatRoom = getChatRoom(chatRoomId);
        if(chatRoom.isExitedMember(removedById)) {
            return false;
        }
        if(chatRoom.getChatRoomType()==ChatRoomType.DIRECT){
            return false;
        }

        List<User> users = sharedService.getAllUsers(memberIds);

        if (users.size() != memberIds.size()) {
            return false;
        }

        User removedByUser = sharedService.getUser(removedById);

        for (User user : users) {
            // Notify exit message to group
            notifyGroupAboutMember(chatRoom,user,removedByUser,false);
            chatRoom.exitGroup(user.getUserId());
            webSocketMessageService.sendExitMemberStatus(chatRoomId,user.getUserId(),user.getUsername(),removedByUser.getUsername());
        }

        boolean membersRemoved = chatRoom.getUserIds().removeAll(memberIds);

        if (membersRemoved) {
            chatRoomRepository.save(chatRoom);
            return true;
        } else {
            return false;
        }
    }

    private void notifyGroupAboutMember(ChatRoom chatRoom, User removedUser, User removedByUser,Boolean isAdded) {
        StringBuilder content = new StringBuilder(removedByUser.getUsername());
        if(!isAdded) {
            if(removedUser.equals(removedByUser)){
                content.append(" exited group");
            } else {
                content.append(" removed "+ removedUser.getUsername());
            }
        } else {
            if(removedUser.equals(removedByUser)){
                content.append(" joined");
            } else {
                content.append(" added "+ removedUser.getUsername());
            }
        }
        userService.saveUser(removedUser);
        ChatMessage message = ChatMessage.builder().senderId(removedByUser.getUserId()).user(removedUser).status(MessageStatus.PENDING).deliveredRecipients(new HashSet<>()).readRecipients(new HashSet<>()).content(content.toString()).type(MessageType.EXITED).chatRoomId(chatRoom.getId()).timestamp(Instant.now()).build();
        ChatMessage savedMessage = chatMessageRepository.save(message);
        webSocketMessageService.sendMessage(chatRoom.getId(),savedMessage);
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

    public void processChatRoomToDB(ChatRoom chatRoom) {
        if(chatRoom.getDeletedForUsers().size()==chatRoom.getExitedMembers().size() && chatRoom.getUserIds().isEmpty()) {
            chatRoomRepository.deleteById(chatRoom.getId());
        } else {
            chatRoomRepository.save(chatRoom);
        }
    }

    public Boolean deleteChat(String chatRoomId, String userId) {
        ChatRoom chatRoom = getChatRoom(chatRoomId);
        userService.groupJoinedOrLeft(userId,chatRoom.getId(),false);
        clearChat(chatRoom, userId);
        processChatRoomToDB(chatRoom);
        redisChatRoomService.removeUserFromChatRoom(userId);
        return true;
    }

    public OnlineUsersDto getOnlineUsers(String chatRoomId, String userId) {
        ChatRoom chatRoom = getChatRoom(chatRoomId);
        Set<String> onlineUsers = getOnlineUsersOfGroup(chatRoom);
        Instant lastSeenTimstamp = null;
        // If ChatRoom is Individual get Last Seen
        if(chatRoom.getChatRoomType()== ChatRoomType.DIRECT)
            lastSeenTimstamp=getLastSeenTimeStampOfCouterPartUser(chatRoom,userId);
        OnlineUsersDto response = OnlineUsersDto.builder().onlineUsers(onlineUsers).lastSeenTimestamp(lastSeenTimstamp).build();
        return response;
    }
}