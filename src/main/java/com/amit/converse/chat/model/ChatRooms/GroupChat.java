package com.amit.converse.chat.model.ChatRooms;

import com.amit.converse.chat.Interface.ITransactable;
import com.amit.converse.chat.model.Enums.ChatRoomType;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.*;

@Data
@Builder
@TypeAlias("GROUP")
@EqualsAndHashCode(callSuper = false)
@Document(collection = "chatRooms")
    public class GroupChat extends ChatRoom implements ITransactable {

    public GroupChat(String name, String createdBy, List<String> adminUserIds, Boolean isExited, Map<String, Instant> exitedMembers, Map<String, Instant> blindPeriod) {
        super(ChatRoomType.GROUP);
        this.name = name;
        this.createdBy = createdBy;
        this.adminUserIds = adminUserIds;
        this.isExited = isExited;
        this.exitedMembers = exitedMembers;
        this.blindPeriod = blindPeriod;
    }

    @NotBlank
    private String name;
    private String createdBy;
    // Admin UserIds for Group only
    private List<String> adminUserIds;
    // Exit Group Feature Only For Groups
    private transient Boolean isExited;

    @Builder.Default
    private Map<String,Instant> exitedMembers = new HashMap<>();

    @Builder.Default
    private Map<String,Instant> blindPeriod = new HashMap<>();

    public GroupChat() {
        super(ChatRoomType.GROUP);
    }

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
        return getExitedMemberCount()==super.getDeletedForUsersCount();
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
