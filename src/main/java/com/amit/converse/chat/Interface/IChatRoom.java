package com.amit.converse.chat.Interface;

import com.amit.converse.chat.model.Enums.ChatRoomType;
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
    Map<String, Instant> getUserFetchStartTimeMap();
    Instant getCreatedAt();
    Set<String> getDeletedForUsers();
    ChatRoomType getChatRoomType();
    void clearChat(String userId);
    void deleteChat(String userId);
}