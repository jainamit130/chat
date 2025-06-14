package com.amit.converse.chat.model.ChatRooms;

import com.amit.converse.chat.model.Enums.ChatRoomType;
import lombok.*;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.TypeAlias;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Setter
@TypeAlias("DIRECT")
@EqualsAndHashCode(callSuper = false)
public class DirectChat extends ChatRoom {

    @Transient
    private String counterPartUserId;

    public DirectChat(String counterPartUsername) {
        super(ChatRoomType.DIRECT, new ArrayList<>());
        this.chatRoomName = counterPartUsername;
    }

    @PersistenceCreator
    public DirectChat(List<String> userIds) {
        super(ChatRoomType.DIRECT,userIds);
    }

    @Override
    public Boolean isDeletable() {
        return !isNewlyFormed && super.getTotalMemberCount()==super.getDeletedForUsersCount();
    }
    }


/*
*
* User1
* User2
*
* */