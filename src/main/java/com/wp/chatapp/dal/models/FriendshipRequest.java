package com.wp.chatapp.dal.models;

import com.wp.chatapp.business.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "friendshipRequest")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendshipRequest {
    private String senderId;
    private String receiverId;
    private RequestStatus status;
}
