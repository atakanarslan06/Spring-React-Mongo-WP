package com.wp.chatapp.business.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Builder
public class MessageDto {
    private String id;
    private String senderId;
    private String receiverId;
    private String groupId;
    private String content;
    private LocalDateTime timestamp;
    private boolean groupMessage;
    private boolean deleted;
}
