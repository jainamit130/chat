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

    @Query("{ 'chatRoomId': ?0, 'timestamp': { $gt: ?1, $lt: ?2 }, 'deletedForUsers': { $ne: ?3 } }")
    List<ChatMessage> findMessagesWithPaginationAfterTimestamp(
            String chatRoomId, Instant fetchFromTimestamp, Instant toTimestamp, String userId, Pageable pageable
    );

    @Aggregation(pipeline = {
            "{ $match: { 'chatRoomId': ?0, 'timestamp': { $gt: ?1, $lt: ?2 }, 'deletedForUsers': { $nin: [?3] } }}",
            "{ $sort: { 'timestamp': -1 } }",
            "{ $limit: 1 }"
    })
    ChatMessage findLatestMessage(String chatRoomId,Instant fetchFromTimestamp,Instant toTimestamp, String userId);


    @Query(value = "{ 'chatRoomId': ?0,'timestamp': { $gt: ?1, $lt: ?2 },'deletedForUsers': { $nin: [?3] } }", count = true)
    Integer countByChatRoomIdAndNotDeletedForUser(String chatRoomId, Instant fetchFromTimestamp, Instant toTimestamp, String userId);

    @Query(value = "{ 'chatRoomId': ?0,'type':'MESSAGE','timestamp': { $lt: ?1 } }", count = true)
    Integer countMessagesOfChatRoomIdToTimestamp(String chatRoomId, Instant toTimestamp);

    List<ChatMessage> findByTimestampBetween(Instant start, Instant end);

    @Aggregation(pipeline = {
            "{ '$match': { 'chatRoomId': ?0, 'type': 'EXITED', 'user.userId': ?1 } }",
            "{ '$sort': { 'timestamp': -1 } }",
            "{ '$limit': 1 }"
    })
    ChatMessage getLastExitedMessage(String chatRoomId, String userId);
}
