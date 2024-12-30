package com.amit.converse.chat.repository.Message;

import com.amit.converse.chat.model.Messages.Message;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;

public interface IMessageRepository extends MongoRepository<Message,String> {

    @Aggregation(pipeline = {
            "{ $match: { 'chatRoomId': ?0, 'metaData.deletedForUsers': { $nin: [?1] } }}",
            "{ $sort: { 'timestamp': -1 } }",
            "{ $limit: 1 }"
    })
    Message findLatestMessage(String chatRoomId, String userId);

    @Aggregation(pipeline = {
            "{ $match: {'chatRoomId' : ?0 }, 'deletedForUsers': { $nin: [?1] }, 'timestamp': { $gt: ?2 } }",
            "{ $sort: { 'timestamp': 1 } }"
    })
    List<Message> findMessagesOfChatForUserFrom(String chatRoomId, String userId, Instant from);
}
