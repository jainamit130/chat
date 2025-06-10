package com.amit.converse.chat.model.ChatRooms;

import com.amit.converse.chat.Interface.ITransactable;
import com.amit.converse.chat.dto.OnlineUsers.GroupChatOnlineUsersDTO;
import com.amit.converse.chat.dto.OnlineUsers.IOnlineUsersDTO;
import com.amit.converse.chat.model.Enums.ChatRoomType;
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
    public GroupChat(String name, List<String> userIds, String createdBy, List<String> adminUserIds) {
        super(name,ChatRoomType.GROUP,userIds);
        this.name = name;
        this.adminUserIds = adminUserIds;
        this.createdBy=createdBy;
        this.exitedMembers = new HashMap<>();
        this.blindPeriods = new HashMap<>();
    }

    private final String name;
    private String createdBy;
    // Admin UserIds for Group only
    private List<String> adminUserIds;
    // Exit Group Feature Only For Groups
    @Transient
    private Boolean isExited;
    private Map<String,Instant> exitedMembers;
    private Map<String,List<BlindPeriod>> blindPeriods;

    @Override
    public Integer getExitedMemberCount() {
        return exitedMembers.size();
    }

    @Override
    public Boolean isExited(String userId) {
        return exitedMembers.containsKey(userId);
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
    public void join(List<String> userIds) {
        unExit(userIds);
        HashSet<String> userIdsSet = new HashSet(this.userIds);
        userIds.addAll(userIdsSet);
    }

    public void markExited() {
        isExited = true;
    }

    @Override
    public void exit(List<String> userIds) {
        userIds.forEach((userId) -> {
                if(!exitedMembers.containsKey(userId))
                    exitedMembers.put(userId,Instant.now());
        });
    }

    private void unExit(List<String> userIds) {
        userIds.forEach(userId -> {
            if (exitedMembers.containsKey(userId)) {
                List<BlindPeriod> blindPeriods = this.blindPeriods.computeIfAbsent(userId, k -> new ArrayList<>());
                blindPeriods.add(new BlindPeriod(exitedMembers.get(userId), Instant.now()));
                exitedMembers.remove(userId);
            }
        });
    }

    @Override
    public void readMessages(String userId) {
        super.readMessages(userId);
        if(exitedMembers.containsKey(userId)) {
            exitedMembers.put(userId,Instant.now());
        }
    }

    @Override
    public IOnlineUsersDTO transit() {
        if(isExited) return new GroupChatOnlineUsersDTO();
        return super.transit();
    }

    public Instant getExitInstant(String userId) {
        return exitedMembers.get(userId);
    }
}
