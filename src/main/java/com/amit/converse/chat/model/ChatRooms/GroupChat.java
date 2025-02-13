package com.amit.converse.chat.model.ChatRooms;

import com.amit.converse.chat.Interface.ITransactable;
import com.amit.converse.chat.model.Enums.ChatRoomType;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
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
        super(ChatRoomType.GROUP,userIds);
        this.name = name;
        this.adminUserIds = Collections.singletonList(adminUserId);
        this.createdBy=adminUserId;
        this.isExited = false;
        this.exitedMembers = new HashMap<>();
        this.blindPeriod = new HashMap<>();
    }


    /*
    * In a List of chatrooms
    * i want to use the chatRoom and do the respective based on the instance
    *
    * Direct Chat -> fetch counterpart username and set chatRoom chatRoomName
    * Group Chat -> fetch its own chatRoomName and set chatRoom chatRoomName
    * Self Chat -> fetch its own chatRoomName and set chatRoom chatRoomName
    *
    * */

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
