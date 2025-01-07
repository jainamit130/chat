package com.amit.converse.chat.repository.ChatRoom;

import com.amit.converse.chat.Interface.ITransactable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IGroupRepository extends MongoRepository<ITransactable,String> {
}
