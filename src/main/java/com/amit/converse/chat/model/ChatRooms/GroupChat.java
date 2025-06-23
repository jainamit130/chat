package com.amit.converse.chat.model.ChatRooms;

import com.amit.converse.chat.Interface.ITransactable;
import com.amit.converse.chat.model.Enums.ChatRoomType;
import com.amit.converse.chat.model.Messages.Message;
import com.amit.converse.chat.model.User;
import lombok.*;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.*;

@Getter
@Setter
@TypeAlias("GROUP")
@EqualsAndHashCode(callSuper = false)
@Document(collection = "chatRooms")
public class GroupChat extends ChatRoom implements ITransactable {

    public GroupChat(String name, List<String> userIds, String adminUserId) {
        super(name,ChatRoomType.GROUP,userIds);
        this.name = name;
        this.adminUserIds = Collections.singletonList(adminUserId);
        this.createdBy=adminUserId;
        this.exitedMembers = new HashMap<>();
        this.blindPeriods = new HashMap<>();
    }

    @PersistenceCreator
    public GroupChat(String name, List<String> userIds, String createdBy, Map<String,Instant> exitedMembers, Map<String,List<BlindPeriod>> blindPeriods, List<String> adminUserIds) {
        super(name,ChatRoomType.GROUP,userIds);
        this.name = name;
        this.adminUserIds = adminUserIds;
        this.createdBy = createdBy;
        this.exitedMembers = exitedMembers;
        this.blindPeriods = blindPeriods;
    }

    private final String name;

    private String createdBy;
    // Admin UserIds for Group only
    private List<String> adminUserIds;
    // Exit Group Feature Only For Groups
    @Transient
    private Boolean isExited;
    @Builder.Default
    private Map<String,Instant> exitedMembers = new HashMap<>();
    @Builder.Default
    private Map<String,List<BlindPeriod>> blindPeriods = new HashMap<>();
    @Override
    public Integer getExitedMemberCount() {
        return exitedMembers.size();
    }

    public List<BlindPeriod> getBlindPeriodsOfUser(String userId) {
        return new ArrayList<>(blindPeriods.getOrDefault(userId, new ArrayList<>()));
    }

    @Override
    public Boolean isExited(String userId) {
        return exitedMembers.containsKey(userId);
    }

    public Boolean isPartOfGroup(String userId) {
        return userIds.contains(userId);
    }

    public void clearBlindPeriod(String userId) {
        Map<String,List<BlindPeriod>> blindPeriod = this.getBlindPeriods();
        blindPeriod.remove(userId);
        setBlindPeriods(blindPeriod);
    }

    @Override
    public void clearChat(String userId) {
        clearBlindPeriod(userId);
        super.clearChat(userId);
    }

    @Override
    public Boolean isDeletable() {
        return !isNewlyFormed && (super.getDeletedForUsersCount()==getTotalMemberCount());
    }

    @Override
    public Integer getTotalMemberCount() {
        return super.getTotalMemberCount() + getExitedMemberCount();
    }

    @Override
    public void join(List<String> userIds, boolean shareChatHistory) {
        unExit(userIds,shareChatHistory);
        HashSet<String> userIdsSet = new HashSet(this.userIds);
        userIds.addAll(userIdsSet);
    }

    @Override
    public void deleteChat(String userId) {
        userIds.remove(userId);
        if(exitedMembers.containsKey(userId)) {
            exitedMembers.remove(userId);
            memberLatestMessage.remove(userId);
        } else {
            deletedForUsers.add(userId);
        }
    }

    @Override
    public void exit(List<String> userIds) {
        userIds.forEach((userId) -> {
                if(!exitedMembers.containsKey(userId))
                    exitedMembers.put(userId,Instant.now().minusMillis(5));
        });
        isExited = true;
    }

    private void unExit(List<String> userIds,boolean shareChatHistory) {
        userIds.forEach(userId -> {
            if (exitedMembers.containsKey(userId)) {
                if(!shareChatHistory) {
                    List<BlindPeriod> blindPeriods = this.blindPeriods.computeIfAbsent(userId, k -> new ArrayList<>());
                    blindPeriods.add(new BlindPeriod(exitedMembers.get(userId), Instant.now().plusMillis(5)));
                }
                exitedMembers.remove(userId);
            } else {
                if(!shareChatHistory) {
                    userFetchStartTimeMap.put(userId,Instant.now().plusMillis(5));
                    List<BlindPeriod> blindPeriods = this.blindPeriods.computeIfAbsent(userId, k -> new ArrayList<>());
                    blindPeriods.add(new BlindPeriod(createdAt,userFetchStartTimeMap.get(userId)));
                } else {
                    userFetchStartTimeMap.put(userId,createdAt);
                }
            }
        });
        this.isExited=false;
    }

    @Override
    public void readMessages(String userId) {
        super.readMessages(userId);
        if(exitedMembers.containsKey(userId)) {
            exitedMembers.put(userId,Instant.now());
        }
    }

    public Instant getLastAvailableInstant(String userId) {
        if (exitedMembers.containsKey(userId)) {
            return exitedMembers.get(userId);
        } else {
            List<BlindPeriod> blindPeriods = this.blindPeriods.getOrDefault(userId,Collections.singletonList(new BlindPeriod(createdAt,getUserFetchStartTime(userId))));
            return blindPeriods.getLast().getEnd();
        }
    }
}
