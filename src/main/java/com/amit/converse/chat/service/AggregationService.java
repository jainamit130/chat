package com.amit.converse.chat.service;

import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;


@Service
public class AggregationService {
    @Autowired
    private MongoTemplate mongoTemplate;

    public List<ChatRoom> getFulFilledChatRooms(List<String> chatRoomIds, String userId) {
        // Aggregation pipeline to fetch chat rooms with all fields
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("_id").in(chatRoomIds)),
                Aggregation.match(Criteria.where("userIds").is(userId).and("deletedForUsers").nin(userId))

        );


        // Execute the aggregation
        AggregationResults<ChatRoom> results = mongoTemplate.aggregate(aggregation, "chatRooms", ChatRoom.class);

        // Return the result
        return results.getMappedResults();
    }
}
