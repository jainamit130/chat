package com.amit.converse.chat.repository.ChatRoom;

import com.amit.converse.chat.model.ChatRooms.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IChatRoomRepository extends MongoRepository<ChatRoom, String> {
}
