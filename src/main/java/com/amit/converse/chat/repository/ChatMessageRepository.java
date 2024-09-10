package com.amit.converse.chat.repository;

import com.amit.converse.chat.model.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage,String> {
    List<ChatMessage> findAllByChatRoomId(String chatRoomId);
    ChatMessage findTopByChatRoomIdOrderByTimestampDesc(String chatRoomId);

    @Query("{ 'chatRoomId': ?0 }")
    List<ChatMessage> findMessagesByChatRoomId(String chatRoomId, Pageable pageable);
}
