package com.amit.converse.chat.repository.ChatRoom;

import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IChatRoomRepository extends MongoRepository<ChatRoom, String> {

    @Aggregation(pipeline = {
            "{ $match: { '_id': { $in: ?0 }, 'deletedForUsers': { $nin: [?1] } } }"
    })
    List<ChatRoom> getAllChatRoomsByIds(List<String> chatRoomIds, String userId);
}
