package com.wp.chatapp.dal.repositories;

import com.wp.chatapp.dal.models.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends MongoRepository<Group, String> {

}
