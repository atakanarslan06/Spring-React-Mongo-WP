package com.wp.chatapp.dal.repositories;

import com.wp.chatapp.dal.models.FriendshipRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface FriendshipRequestRepository extends MongoRepository<FriendshipRequest, String> {
    boolean existsBySenderIdAndReceiverId(String id, String receiverId);
}
