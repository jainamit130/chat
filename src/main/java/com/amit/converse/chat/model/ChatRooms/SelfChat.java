package com.amit.converse.chat.model.ChatRooms;

import com.amit.converse.chat.model.Enums.ChatRoomType;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.TypeAlias;

import java.util.List;

@TypeAlias("SELF")
@EqualsAndHashCode(callSuper = false)
public class SelfChat extends ChatRoom {

    @PersistenceCreator
    public SelfChat(List<String> userIds, String name) {
        super(ChatRoomType.SELF, userIds);
        this.name = name;
    }

    public SelfChat(String name, List<String> userIds) {
        super(ChatRoomType.SELF,userIds);
        this.name = name;
    }

    private final String name;

    // Cannot delete self chats from repository
    @Override
    public Boolean isDeletable() {
        return false;
    }

}
