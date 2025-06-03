package com.amit.converse.chat.repository.ChatRoom;

import com.amit.converse.chat.model.ChatRooms.DirectChat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IDirectChatRepository extends MongoRepository<DirectChat, String> {

    @Query("{ 'chatRoomType': 'DIRECT', $or: [ " +
            "{ 'userIds': { $all: [?0, ?1] } }, " +
            "{ 'deletedForUsers': { $all: [?0, ?1] } }, " +
            "{ $and: [ { 'userIds': ?0 }, { 'deletedForUsers': ?1 } ] }, " +
            "{ $and: [ { 'userIds': ?1 }, { 'deletedForUsers': ?0 } ] } " +
            "] }")
    Optional<DirectChat> findDirectChat(String userId1, String userId2);
    
}
