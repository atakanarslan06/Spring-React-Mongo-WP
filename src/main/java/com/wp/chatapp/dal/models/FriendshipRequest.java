package com.wp.chatapp.dal.models;

import com.wp.chatapp.business.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "friendshipRequest")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendshipRequest {
    @Id
    private String id;
    private String senderId;
    private String receiverId;
    private RequestStatus status;
    private String senderUsername;
}
