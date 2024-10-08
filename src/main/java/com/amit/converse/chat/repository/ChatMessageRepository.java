package com.amit.converse.chat.repository;

import com.amit.converse.chat.model.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage,String> {
    List<ChatMessage> findAllByChatRoomIdOrderByTimestampDesc(String chatRoomId);

    @Query("{ 'chatRoomId': ?0 }")
    List<ChatMessage> findMessagesWithPagination(String chatRoomId, Pageable pageable);
    ChatMessage findTopByChatRoomIdOrderByTimestampDesc(String chatRoomId);

    Integer countByChatRoomId(String chatRoomId);
}
