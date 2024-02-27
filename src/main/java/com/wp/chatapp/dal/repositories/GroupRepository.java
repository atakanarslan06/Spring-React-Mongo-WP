package com.wp.chatapp.dal.repositories;

import com.wp.chatapp.dal.models.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends MongoRepository<Group, String> {
    List<Group> findByIsActiveTrue();
    Optional<Group> findByIdAndActiveTrue(String id);
}
