package com.amit.converse.chat.model.ChatRooms;

import lombok.*;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class DirectChat extends ChatRoom {
    private transient String user;
    private transient String counterPartUser;

    public DirectChat(String user) {

    }
}
