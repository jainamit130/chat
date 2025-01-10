package com.amit.converse.chat.repository.ChatRoom;

import com.amit.converse.chat.model.ChatRooms.DirectChat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;


public interface IDirectChatRepository extends MongoRepository<DirectChat, String> {

    @Query("{ 'chatRoomType' : 'DirectChat', 'userIds' : { $all: [?0, ?1] } }")
    Optional<DirectChat> findDirectChat(String userId1, String userId2);
    
}
