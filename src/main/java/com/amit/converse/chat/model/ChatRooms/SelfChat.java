package com.amit.converse.chat.model.ChatRooms;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "chatRooms")
@EqualsAndHashCode(callSuper = true)
public class SelfChat extends ChatRoom {

    @NotNull
    private String name;

    // Cannot delete self chats from repository
    @Override
    public Boolean isDeletable() {
        return false;
    }
}
