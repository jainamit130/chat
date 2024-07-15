package com.amit.converse.chat.repository;

import com.amit.converse.chat.model.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage,String> {
    List<ChatMessage> findAllByChatRoomId(String chatRoomId);
    ChatMessage findTopByChatRoomIdOrderByTimestampDesc(String chatRoomId);
}
