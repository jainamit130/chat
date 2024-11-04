package com.amit.converse.chat.repository;

import com.amit.converse.chat.model.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends MongoRepository<User,String> {
    boolean existsByUsername(String username);
    Optional<User> findByUserId(String userId);
    List<User> findAllByUsernameStartsWithIgnoreCase(String searchPrefix);
    List<User> findAllByUserIdIn(List<String> userIds);
}
