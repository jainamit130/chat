package com.amit.converse.chat.repository.ChatRoom;

import com.amit.converse.chat.model.ChatRooms.GroupChat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IGroupChatRepository extends MongoRepository<GroupChat,String> {

    @Query("{ 'chatRoomType' : 'GroupChat', 'userIds' : { $all: [?0, ?1] } }")
    Optional<List<GroupChat>> findGroupChats(String userId1, String userId2);
}
