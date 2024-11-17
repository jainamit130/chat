package com.amit.converse.chat.repository;

import com.amit.converse.chat.model.ChatMessage;
import com.amit.converse.chat.model.ChatRoom;
import com.amit.converse.chat.model.ChatRoomType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    List<ChatRoom> findByUserIdsContains(String userId);

    @Query("{ 'chatRoomType' : ?0, 'userIds' : { $all: [ ?1, ?2 ] } }")
    Optional<ChatRoom> findIndividualChatRoomByUserIds(ChatRoomType chatRoomType, String userId1, String userId2);

    @Query("{ 'chatRoomType' : 'SELF', 'userIds' : { $all: [ ?0 ] } }")
    ChatRoom findSelfChatRoomByUserIds(String userId);
}
