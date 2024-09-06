package com.amit.converse.chat.repository;

import com.amit.converse.chat.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User,String> {
    boolean existsByUsername(String username);
    User findByUserId(String userId);
    List<User> findAllByUsernameStartsWithIgnoreCase(String searchPrefix);
}
