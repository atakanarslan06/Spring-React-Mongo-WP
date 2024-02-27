package com.wp.chatapp.dal.repositories;

import com.wp.chatapp.dal.models.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findByReceiverIdOrSenderId(String userId, String userId1);

    List<Message> findBySenderIdOrderByTimestampDesc(String userId);

    List<Message> findByReceiverIdOrderByTimestampDesc(String userId);
}
