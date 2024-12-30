package com.amit.converse.chat.model.ChatRooms;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
@Builder
@Document(collection = "chatRooms")
@EqualsAndHashCode(callSuper = true)
public class GroupChat extends ChatRoom {
    @NotBlank
    private String name;
    private String createdBy;
    // Exit Group Feature Only For Groups
    private transient Boolean isExited;
    private Map<String,Instant> exitedMembers;

    public void addMembers(List<String> userIds) {
        if(!userIds.isEmpty()) {
            this.userIds.addAll(userIds);
        }
    }

    public void removeMembers(List<String> userIds) {
        if(!userIds.isEmpty()) {
            this.userIds.removeAll(userIds);
        }
    }

    public Integer getExitedMemberCount() {
        return exitedMembers.size();
    }

    @Override
    public Boolean isDeletable() {
        return getExitedMemberCount()==super.getDeletedForUsersCount();
    }

    @Override
    public Integer getTotalMemberCount() {
        return super.getTotalMemberCount() + getExitedMemberCount();
    }
}
