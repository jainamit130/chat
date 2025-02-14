package com.amit.converse.chat.model.ChatRooms;

import com.amit.converse.chat.model.Enums.ChatRoomType;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.TypeAlias;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@TypeAlias("SELF")
@EqualsAndHashCode(callSuper = false)
public class SelfChat extends ChatRoom {

    @PersistenceCreator
    public SelfChat(String id, List<String> userIds, ChatRoomType chatRoomType, Instant createdAt, Map<String, Instant> userFetchStartTimeMap, Map<String, Instant> lastVisitedTimestamp, String name) {
        super(id, userIds, chatRoomType, createdAt, userFetchStartTimeMap, lastVisitedTimestamp);
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
