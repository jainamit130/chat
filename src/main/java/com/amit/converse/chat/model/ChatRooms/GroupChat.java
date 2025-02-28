package com.amit.converse.chat.model.ChatRooms;

import com.amit.converse.chat.Interface.ITransactable;
import com.amit.converse.chat.Redis.ChatRoomRedisTransitionService;
import com.amit.converse.chat.model.Enums.ChatRoomType;
import lombok.*;
import org.springframework.data.annotation.PersistenceCreator;
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
        this.isExited = false;
        this.exitedMembers = new HashMap<>();
        this.blindPeriod = new HashMap<>();
    }

    @PersistenceCreator
    public GroupChat(String name, List<String> userIds, String createdBy, List<String> adminUserIds) {
        super(name,ChatRoomType.GROUP,userIds);
        this.name = name;
        this.adminUserIds = adminUserIds;
        this.createdBy=createdBy;
        this.isExited = false;
        this.exitedMembers = new HashMap<>();
        this.blindPeriod = new HashMap<>();
    }

    private final String name;
    private String createdBy;
    // Admin UserIds for Group only
    private List<String> adminUserIds;
    // Exit Group Feature Only For Groups
    private transient Boolean isExited;
    private Map<String,Instant> exitedMembers;
    private Map<String,Instant> blindPeriod;

    @Override
    public Integer getExitedMemberCount() {
        return exitedMembers.size();
    }

    @Override
    public Boolean isExited(String userId) {
        return exitedMembers.containsKey(userId);
    }

    public void clearBlindPeriod(String userId) {
        Map<String,Instant> blindPeriod = getBlindPeriod();
        blindPeriod.remove(userId);
        setBlindPeriod(blindPeriod);
    }

    @Override
    public void clearChat(String userId) {
        clearBlindPeriod(userId);
        super.clearChat(userId);
    }

    @Override
    public Boolean isDeletable() {
        return getUserIds().isEmpty() && getExitedMemberCount()==super.getDeletedForUsersCount();
    }

    @Override
    public Integer getTotalMemberCount() {
        return super.getTotalMemberCount() + getExitedMemberCount();
    }

    @Override
    public void join(List<String> userIds) {
        unExit(userIds);
        HashSet<String> userIdsSet = new HashSet(this.userIds);
        userIdsSet.addAll(userIds);
        setUserIds(new ArrayList<>(userIdsSet));
    }

    @Override
    public void exit(List<String> userIds) {
        userIds.forEach((userId) -> {
                if(!exitedMembers.containsKey(userId))
                    exitedMembers.put(userId,Instant.now());
        });
    }

    private void unExit(List<String> userIds) {
        userIds.forEach(exitedMembers::remove);
    }
}
