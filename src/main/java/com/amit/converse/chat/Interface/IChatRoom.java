package com.amit.converse.chat.Interface;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Document(collection = "chatRooms")
public interface IChatRoom {
    List<String> getUserIds();
    Integer getMemberCount();
    Integer getTotalMemberCount();
    Boolean isDeletable();
    String getId();
    Instant getUserFetchStartTime(String userId);
    Instant getLastVisitedTimestamp(String userId);
    Integer getUnreadMessageCount(String userId);
    Instant getCreatedAt();
    Set<String> getDeletedForUsers();
    void clearChat(String userId);
    void deleteChat(String userId);
}