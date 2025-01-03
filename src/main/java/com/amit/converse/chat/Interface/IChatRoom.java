package com.amit.converse.chat.Interface;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chatRooms")
public interface IChatRoom {
    Integer getMemberCount();
    Integer getTotalMemberCount();
    Boolean isDeletable();
    String getId();
}