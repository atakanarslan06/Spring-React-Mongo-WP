package com.wp.chatapp.dal.repositories;

import com.wp.chatapp.business.dto.MessageDto;
import com.wp.chatapp.dal.models.Message;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findByReceiverIdOrSenderId(String userId, String userId1);
    List<Message> findBySenderIdOrderByTimestampDesc(String userId);
    List<Message> findByReceiverIdOrderByTimestampDesc(String userId);

    @Aggregation({
            "{ $match: { $or: [ { senderId: ?0 }, { receiverId: ?0 } ] } }",
            "{ $sort: { timestamp: -1 } }",
            "{ $group: { _id: { $cond: [ { $eq: ['$senderId', ?0] }, '$receiverId', '$senderId' ] }, message: { $first: '$message' }, timestamp: { $first: '$timestamp' } } }",
            "{ $project: { _id: 0, userId: '$_id', message: 1, timestamp: 1 } }"
    })
    List<MessageDto> getLastMessagesForEachUser(String userId);
}
