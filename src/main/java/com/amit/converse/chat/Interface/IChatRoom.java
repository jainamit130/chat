package com.amit.converse.chat.Interface;

import com.amit.converse.chat.dto.OnlineUsers.IOnlineUsersDTO;
import com.amit.converse.chat.model.Enums.ConnectionStatus;
import com.amit.converse.chat.model.Messages.ChatMessage;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.Set;

@Document(collection = "chatRooms")
public interface IChatRoom {
    List<String> getUserIds();
    Integer getMemberCount();
    Integer getTotalMemberCount();
    Boolean isDeletable();
    String getId();
    void setLatestMessage(ChatMessage message);
    Instant getUserFetchStartTime(String userId);
    void readMessages(String userId);
    Instant getLastVisitedTimestamp(String userId);
    Integer getUnreadMessageCount(String userId);
    Instant getCreatedAt();
    Set<String> getDeletedForUsers();
    void clearChat(String userId);
    void deleteChat(String userId);
    IOnlineUsersDTO transit();
}