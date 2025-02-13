package com.amit.converse.chat.model.ChatRooms;

import com.amit.converse.chat.model.Enums.ChatRoomType;
import lombok.*;
import org.springframework.data.annotation.TypeAlias;

import java.util.List;

@TypeAlias("DIRECT")
@EqualsAndHashCode(callSuper = false)
public class DirectChat extends ChatRoom {

    public DirectChat(String primaryUserId, String counterPartUserId , String counterPartUsername) {
        super(ChatRoomType.DIRECT,List.of(primaryUserId,counterPartUserId));
        this.chatRoomName = counterPartUsername;
    }

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