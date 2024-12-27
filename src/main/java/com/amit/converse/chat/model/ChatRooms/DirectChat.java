package com.amit.converse.chat.model.ChatRooms;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class DirectChat extends ChatRoom {
    @NotBlank
    private transient String username;
    @NotBlank
    private transient String counterPartUsername;
}


/*
*
* User1
* User2
*
* */