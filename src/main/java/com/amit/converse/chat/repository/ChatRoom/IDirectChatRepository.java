package com.amit.converse.chat.repository.ChatRoom;

import com.amit.converse.chat.model.ChatRooms.DirectChat;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface IDirectChatRepository extends MongoRepository<DirectChat, String> {

}
