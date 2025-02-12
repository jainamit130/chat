package com.amit.converse.chat.repository.ChatRoom;

import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IChatRoomRepository extends MongoRepository<ChatRoom, String> {

    @Aggregation(pipeline = {
            "{ $match: { '_id': { $in: ?0 }}}",  // Match chat rooms

            // Project all necessary fields
            "{ $project: { " +
                    "'totalMessageCount': 1, " +
                    "'readMessageCount': 1, " +
                    "'userReadCount': { $ifNull: [ { $arrayElemAt: [ '$readMessageCount.?1', 0 ] }, 0 ] }, " +
                    "'userIds': 1, " +
                    "'chatRoomType': 1, " +
                    "'createdAt': 1, " +
                    "'userFetchStartTimeMap': 1, " +
                    "'deletedForUsers': 1, " +
                    "'lastVisitedTimestamp': 1 " +
                    "}}",

            // Calculate unreadMessageCount (totalMessageCount - userReadCount)
            "{ $addFields: { 'unreadMessageCount': { $subtract: [ '$totalMessageCount', '$userReadCount' ] }}}",

            // Final projection to include all fields
            "{ $project: { " +
                    "'_id': 1, " +  // Include _id explicitly
                    "'unreadMessageCount': 1, " +
                    "'userIds': 1, " +
                    "'chatRoomType': 1, " +
                    "'createdAt': 1, " +
                    "'userFetchStartTimeMap': 1, " +
                    "'deletedForUsers': 1, " +
                    "'lastVisitedTimestamp': 1 " +
                    "}}"
    })
    List<ChatRoom> getAllChatRooms(List<String> chatRoomIds, String userId);


}
