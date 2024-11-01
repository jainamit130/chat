package com.amit.converse.chat.repository;

import com.amit.converse.chat.model.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage,String> {
    List<ChatMessage> findAllByChatRoomIdOrderByTimestampDesc(String chatRoomId);

    @Query(value = "{ 'chatRoomId': ?0, 'timestamp': { $gt: ?1 }, 'deletedForUsers': { $nin: [?2] } }", sort = "{ 'timestamp': 1 }")
    List<ChatMessage> findMessagesWithPaginationAfterTimestamp(
            String chatRoomId, Instant timestamp, String userId, Pageable pageable
    );


    @Query(value = "{ 'chatRoomId': ?0, 'deletedForUsers': { $nin: [?1] } }", sort = "{ 'timestamp': -1 }")
    ChatMessage findTopByChatRoomIdAndNotDeletedForUserOrderByTimestampDesc(
            String chatRoomId, String userId
    );



    @Query(value = "{ 'chatRoomId': ?0, 'deletedForUsers': { $nin: [?1] } }", count = true)
    Integer countMessagesByChatRoomIdAndNotDeletedForUser(String chatRoomId, String userId);

}
