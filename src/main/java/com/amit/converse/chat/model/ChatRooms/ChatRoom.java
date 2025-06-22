package com.amit.converse.chat.model.ChatRooms;

import com.amit.converse.chat.Interface.IChatRoom;
import com.amit.converse.chat.Redis.ChatRoomRedisTransitionService;
import com.amit.converse.chat.dto.OnlineUsers.IOnlineUsersDTO;
import com.amit.converse.chat.model.Enums.ChatRoomType;
import com.amit.converse.chat.model.Messages.Message;
import com.amit.converse.chat.model.User;
import com.amit.converse.chat.service.chatRoom.filfillmentService.ChatRoomFulfilmentService;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.*;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = DirectChat.class, name = "DIRECT"),
        @JsonSubTypes.Type(value = GroupChat.class, name = "GROUP"),
        @JsonSubTypes.Type(value = SelfChat.class, name = "SELF")
})
@Document(collection = "chatRooms")
public abstract class ChatRoom implements IChatRoom {

    public ChatRoom(String name,ChatRoomType chatRoomType, List<String> userIds) {
        this.chatRoomName = name;
        this.chatRoomType = chatRoomType;
        this.userIds = userIds;
        this.createdAt = Instant.now();
        this.lastVisitedTimestamp = new HashMap<>();
        this.deletedForUsers = new HashSet<>();
        this.userFetchStartTimeMap = new HashMap<>();
        this.totalMessageCount = 0;
        this.readMessageCount = new HashMap<>();
    }

    public ChatRoom(ChatRoomType chatRoomType, List<String> userIds) {
        this.chatRoomType = chatRoomType;
        this.userIds = userIds;
        this.createdAt = Instant.now();
        this.lastVisitedTimestamp = new HashMap<>();
        this.deletedForUsers = new HashSet<>();
        this.userFetchStartTimeMap = new HashMap<>();
        this.totalMessageCount = 0;
        this.readMessageCount = new HashMap<>();
    }

    @Id
    protected String id;
    protected List<String> userIds;
    protected ChatRoomType chatRoomType;
    protected Instant createdAt;
    // Clear Chat Feature
    protected Map<String, Instant> userFetchStartTimeMap;
    protected Set<String> deletedForUsers;
    protected Integer totalMessageCount;
    protected Map<String, Message> memberLatestMessage;
    protected Map<String, Integer> readMessageCount;
    protected Map<String, Instant> lastVisitedTimestamp;
    @Transient
    protected String chatRoomName;
    @Transient
    protected Integer unreadMessageCount;
    @Transient
    protected Message latestMessage;
    @Transient
    protected ChatRoomFulfilmentService chatRoomFulfilmentService;
    @Transient
    protected ChatRoomRedisTransitionService chatRoomRedisTransitionService;
    @Transient
    protected Boolean isNewlyFormed = false;

    public List<String> getAllUserIds() {
        ArrayList<String> userIds = new ArrayList<>(this.userIds);
        userIds.addAll(deletedForUsers);
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        Set<String> userIdsSet = Set.copyOf(userIds);
        this.userIds = new ArrayList<>(userIdsSet);
    }

    public Optional<Message> getLatestMessage(String userId) {
        if(memberLatestMessage==null) memberLatestMessage = new HashMap<>();
        return Optional.ofNullable(memberLatestMessage.get(userId));
    }

    public void updateLatestMessageOfMember(String userId,Message message) {
        memberLatestMessage.put(userId,message);
    }

    public void setName(String name) {
        this.chatRoomName = name;
    }

    public void fulfill(User user) {
        chatRoomFulfilmentService.fulfill(this,user);
    }

    public Integer getDeletedForUsersCount() {
        return getDeletedForUsers().size();
    }

    public abstract Boolean isDeletable();

    public Integer getMemberCount() {
        return userIds.size()+deletedForUsers.size();
    }

    public Integer getTotalMemberCount() {
        return getMemberCount();
    }

    @Override
    public Instant getUserFetchStartTime(String userId) { return userFetchStartTimeMap.getOrDefault(userId,getCreatedAt()); }

    @Override
    public void deleteChat(String userId) {
        userIds.remove(userId);
        deletedForUsers.add(userId);
    }

    @Override
    public void connectChat(String userId) {
        this.userIds.add(userId);
        setUserIds(this.userIds);
        deletedForUsers.remove(userId);
    }

    @Override
    public void disconnectChat(String userId) {
        this.userIds.remove(userId);
        setUserIds(this.userIds);
        deletedForUsers.remove(userId);
    }

    @Override
    public void clearChat(String userId) {
        Map<String,Instant> userFetchStartTimeMap = getUserFetchStartTimeMap();
        userFetchStartTimeMap.put(userId,Instant.now());
        setUserFetchStartTimeMap(userFetchStartTimeMap);
        readMessages(userId);
    }

    public void updateLastVisitedTimestamp(String userId) {
        lastVisitedTimestamp.put(userId,Instant.now());
    }

    @Override
    public Instant getLastVisitedTimestamp(String userId) {
        return lastVisitedTimestamp.getOrDefault(userId,createdAt);
    }

    private Integer getReadMessageCount(String userId) {
        return readMessageCount.getOrDefault(userId,0);
    }

    @Override
    public Integer getUnreadMessageCount(String userId) {
        return getTotalMessageCount()-getReadMessageCount(userId);
    }

    public void totalMessageCountIncrement() {
        totalMessageCount++;
    }

    @Override
    public void readMessages(String userId) {
        updateLastVisitedTimestamp(userId);
        readMessageCount.put(userId,totalMessageCount);
    }

    @Override
    public IOnlineUsersDTO transit() {
        return chatRoomRedisTransitionService.transitAndGetOnlineUsers();
    }

    public Boolean isDeletedForUser(String userId) {
        return deletedForUsers.contains(userId);
    }

    public void updateReadMessageCountOfExitedUser(String userId,Integer unreadMessageCount) {
        readMessageCount.put(userId,getTotalMessageCount() - unreadMessageCount);
    }
}
