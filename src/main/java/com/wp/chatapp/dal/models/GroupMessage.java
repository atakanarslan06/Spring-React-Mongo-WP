package com.wp.chatapp.dal.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(value = "groupMessages")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupMessage {
    @Id
    private String id;
    private String groupId;
    private String senderId;
    private String type;
    private String content;
    private LocalDateTime timestamp;
    private boolean deleted;
}
