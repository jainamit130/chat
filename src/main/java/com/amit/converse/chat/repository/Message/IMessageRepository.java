package com.amit.converse.chat.repository.Message;

import com.amit.converse.chat.model.Messages.Message;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface IMessageRepository extends MongoRepository<Message,String> {

    @Aggregation(pipeline = {
            "{ $match: { 'chatRoomId': ?0, 'messageMetaData.deletedForUsers': { $nin: [?1] }, 'timestamp': { $gt: ?2 } } }",
            "{ $addFields: { status: { $cond: { if: { $eq: [ '$senderId', ?1 ] }, then: '$status', else: null } } } }",
            "{ $sort: { 'timestamp': 1 } }"
    })
    List<Message> findMessagesOfChatForUserFrom(String chatRoomId, String userId, Instant from);

    @Aggregation(pipeline = {
            "{ $match: { 'chatRoomId': ?0, 'messageMetaData.deletedForUsers': { $nin: [?1] } }}",
            "{ $addFields: { status: { $cond: { if: { $eq: [ '$senderId', ?1 ] }, then: '$status', else: null } } } }",
            "{ $sort: { 'timestamp': -1 } }",
            "{ $limit: 1 }"
    })
    Optional<Message> findLatestMessage(String chatRoomId, String userId);
}
