package com.amit.converse.chat.model.ChatRooms;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class DirectChat extends ChatRoom {
    @NotBlank
    private transient String username;

    private List<String> userIds;
    @NotBlank
    private transient String counterPartUsername;
}


/*
*
* User1
* User2
*
* */