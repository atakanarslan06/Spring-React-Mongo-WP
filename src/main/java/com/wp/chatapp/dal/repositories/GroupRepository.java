package com.wp.chatapp.dal.repositories;

import com.wp.chatapp.dal.models.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupRepository extends MongoRepository<Group, String> {

}
