package com.wp.chatapp.dal.repositories;

import com.wp.chatapp.dal.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Optional;
@EnableMongoRepositories
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
}

