package com.amit.converse.chat.repository;

import com.amit.converse.chat.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<User,String> {
    boolean existsByUsername(String username);

    List<User> findAllByUsernameStartsWithIgnoreCase(String searchPrefix);
}
