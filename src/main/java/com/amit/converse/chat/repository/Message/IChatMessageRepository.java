package com.amit.converse.chat.repository.Message;

import com.amit.converse.chat.model.ChatMessage;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.List;

public interface IChatMessageRepository extends MongoRepository<ChatMessage,String> {

    @Aggregation(pipeline = {
            "{ $match: {'chatRoomId' : ?0 }, 'deletedForUsers': { $nin: [?1] }, 'timestamp': { $gt: ?2 } }",
            "{ $sort: { 'timestamp': 1 } }"
    })
    List<ChatMessage> findMessagesOfChatForUserFrom(String chatRoomId, String userId, Instant from);

}
