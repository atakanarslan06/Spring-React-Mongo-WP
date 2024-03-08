package com.wp.chatapp.dal.repositories;

import com.wp.chatapp.business.enums.RequestStatus;
import com.wp.chatapp.dal.models.FriendshipRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendshipRequestRepository extends MongoRepository<FriendshipRequest, String> {
    boolean existsBySenderIdAndReceiverId(String id, String receiverId);

    List<FriendshipRequest> findByReceiverIdAndStatus(String userId, RequestStatus requestStatus);
}
