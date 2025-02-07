package com.amit.converse.chat.model.ChatRooms;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class DirectChat extends ChatRoom {
    @NotBlank
    private transient String name;

    @NotBlank
    private transient String username;

    @NotBlank
    private transient String counterPartUsername;

    @Override
    public Boolean isDeletable() {
        return super.getTotalMemberCount()==super.getDeletedForUsersCount();
    }
}


/*
*
* User1
* User2
*
* */