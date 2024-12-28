package com.amit.converse.chat.repository.ChatRoom;

import com.amit.converse.chat.model.ChatRooms.GroupChat;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IGroupChatRepository extends MongoRepository<GroupChat,String> {


}
