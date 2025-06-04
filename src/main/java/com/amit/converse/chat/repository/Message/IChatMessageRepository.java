package com.amit.converse.chat.repository.Message;

import com.amit.converse.chat.model.Enums.MessageStatus;
import com.amit.converse.chat.model.Messages.ChatMessage;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface IChatMessageRepository extends MongoRepository<ChatMessage,String> {

    @Aggregation(pipeline = {
            "{ $match: { '_class': 'ChatMessage', 'chatRoomId': ?0, 'messageMetaData.deletedForUsers': { $nin: [?1] }, 'timestamp': { $gt: ?2 } } }",
            "{ $addFields: { status: { $cond: { if: { $eq: [ '$senderId', ?1 ] }, then: '$status', else: null } } } }",
            "{ $sort: { 'timestamp': 1 } }"
    })
    List<ChatMessage> findMessagesOfChatForUserFrom(String chatRoomId, String userId, Instant from);

    @Aggregation(pipeline = {
            "{ $match: { '_class': 'ChatMessage',  'chatRoomId': ?0, 'messageMetaData.deletedForUsers': { $nin: [?1] }, 'timestamp': { $gt: ?2 } } }",
            "{ $sort: { 'timestamp': 1 } }"
    })
    List<ChatMessage> findMessagesOfChatForUser(String chatRoomId, String userId, Instant from);

    @Aggregation(pipeline = {
            "{ $match: { '_class': 'ChatMessage', '_id': ?0, 'senderId': ?1, 'messageMetaData.deletedForUsers': { $nin: [?1] } } }",
            "{ $addFields: { " +
                    "status: '$status', " +
                    "deliveryReceiptsByTime: '$messageMetaData.deliveryReceiptsByTime', " +
                    "readReceiptsByTime: '$messageMetaData.readReceiptsByTime' " +
                    "} }"
    })
    Optional<ChatMessage> findMessageById(String messageId, String userId);
}
