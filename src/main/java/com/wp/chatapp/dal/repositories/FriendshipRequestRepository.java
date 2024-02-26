package com.wp.chatapp.dal.repositories;

import com.wp.chatapp.dal.models.FriendshipRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface FriendshipRequestRepository extends MongoRepository<FriendshipRequest, String> {

    boolean existsBySenderIdAndReceiverId(String id, String receiverId);

    Optional<FriendshipRequest> findBySenderIdAndReceiverId(String senderId, String receiverId);
}
