package com.amit.converse.chat.repository.ChatRoom;

import com.amit.converse.chat.model.ChatRooms.SelfChat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ISelfChatRepository extends MongoRepository<SelfChat,String> {

    @Query("{ 'chatRoomType' : 'SelfChat', 'userIds' : { $all: [?0] } }")
    Optional<SelfChat> findSelfChat(String userId);
}
