package com.amit.converse.chat.model.ChatRooms;

import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.model.ChatRoomType;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.*;

public abstract class ChatRoom {
    @Id
    protected String id;
    protected List<String> userIds;
    protected transient ChatMessage latestMessage;
    protected transient Integer unreadMessageCount;
    @NotBlank
    private ChatRoomType chatRoomType;
    // Clear Chat Feature
    private Map<String, Instant> userFetchStartTimeMap;
    private Set<String> deletedForUsers;
    private Integer totalMessagesCount;
    private Map<String, Integer> readMessageCounts;
    private Map<String, Integer> deliveredMessageCounts;

    public Boolean deleteChat(String userId) {
        if(deletedForUsers==null){
            deletedForUsers=new HashSet<>();
        }
        deletedForUsers.add(userId);
        return true;
    }
}
