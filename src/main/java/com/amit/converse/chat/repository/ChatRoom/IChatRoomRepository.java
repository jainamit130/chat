package com.amit.converse.chat.repository.ChatRoom;

import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IChatRoomRepository extends MongoRepository<ChatRoom, String> {
}
