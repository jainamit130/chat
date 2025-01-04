package com.amit.converse.chat.Interface;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

@Document(collection = "chatRooms")
public interface IChatRoom {
    Integer getMemberCount();
    Integer getTotalMemberCount();
    Boolean isDeletable();
    String getId();
    Map<String, Instant> getUserFetchStartTimeMap();
    Instant getCreatedAt();
}