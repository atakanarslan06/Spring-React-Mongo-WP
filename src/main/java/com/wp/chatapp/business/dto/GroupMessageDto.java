package com.wp.chatapp.business.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GroupMessageDto {
    private String id;
    private String groupId;
    private String senderId;
    private String content;
    private LocalDateTime timestamp;
    private boolean deleted;
}
