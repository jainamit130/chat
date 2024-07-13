package com.amit.converse.chat.repository;

import com.amit.converse.chat.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User,String> {
    boolean existsByUsername(String username);
}
