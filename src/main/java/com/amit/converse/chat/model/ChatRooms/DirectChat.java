package com.amit.converse.chat.model.ChatRooms;

import com.amit.converse.chat.model.Enums.ChatRoomType;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;

@Data
@Builder
@TypeAlias("DIRECT")
@EqualsAndHashCode(callSuper = false)
public class DirectChat extends ChatRoom {

    public DirectChat(String name, String username, String counterPartUsername) {
        super(ChatRoomType.DIRECT);
        this.name = name;
        this.username = username;
        this.counterPartUsername = counterPartUsername;
    }

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