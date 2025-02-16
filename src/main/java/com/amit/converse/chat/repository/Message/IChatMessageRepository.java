package com.amit.converse.chat.repository.Message;

import com.amit.converse.chat.model.Messages.ChatMessage;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface IChatMessageRepository extends MongoRepository<ChatMessage,String> {

    @Aggregation(pipeline = {
            "{ $match: {'chatRoomId' : ?0 }, 'deletedForUsers': { $nin: [?1] }, 'timestamp': { $gt: ?2 } }",
            "{ $sort: { 'timestamp': 1 } }"
    })
    List<ChatMessage> findMessagesOfChatForUserFrom(String chatRoomId, String userId, Instant from);

    @Aggregation(pipeline = {
            "{ $match: { 'chatRoomId': ?0, 'deletedForUsers': { $nin: [?1] } }}",
            "{ $sort: { 'timestamp': -1 } }",
            "{ $limit: 1 }"
    })
    Optional<ChatMessage> findLatestMessage(String chatRoomId, String userId);
}
