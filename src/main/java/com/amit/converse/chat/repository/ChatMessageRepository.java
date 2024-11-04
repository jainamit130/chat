package com.amit.converse.chat.repository;

import com.amit.converse.chat.model.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage,String> {
    List<ChatMessage> findAllByChatRoomIdOrderByTimestampDesc(String chatRoomId);

    @Query("{ 'chatRoomId': ?0, 'timestamp': { $gt: ?1 }, 'deletedForUsers': { $ne: ?2 } }")
    List<ChatMessage> findMessagesWithPaginationAfterTimestamp(
            String chatRoomId, Instant fetchFromTimestamp, String userId, Pageable pageable
    );

    @Aggregation(pipeline = {
            "{ $match: { 'chatRoomId': ?0, 'timestamp': { $gt: ?1 }, 'deletedForUsers': { $nin: [?2] } }}",
            "{ $sort: { 'timestamp': -1 } }",
            "{ $limit: 1 }"
    })
    ChatMessage findLatestMessage(String chatRoomId, Instant fetchFromTimestamp, String userId);


    @Query(value = "{ 'chatRoomId': ?0,'timestamp': { $gt: ?1 },'deletedForUsers': { $nin: [?2] } }", count = true)
    Integer countMessagesByChatRoomIdAndNotDeletedForUser(String chatRoomId, Instant fetchFromTimestamp, String userId);

    List<ChatMessage> findByTimestampBetween(Instant start, Instant end);
}
