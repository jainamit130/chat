package com.amit.converse.chat.model.ChatRooms;

import com.amit.converse.chat.model.Enums.ChatRoomType;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;

@Data
@Builder
@TypeAlias("SELF")
@EqualsAndHashCode(callSuper = false)
public class SelfChat extends ChatRoom {

    @NotNull
    private String name;

    public SelfChat(String name) {
        super(ChatRoomType.SELF);
        this.name = name;
    }

    // Cannot delete self chats from repository
    @Override
    public Boolean isDeletable() {
        return false;
    }

}
