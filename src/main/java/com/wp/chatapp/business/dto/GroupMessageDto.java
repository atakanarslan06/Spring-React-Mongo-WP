package com.wp.chatapp.business.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GroupMessageDto {
    private String groupId;
    private String type;
    private String senderId;
    private String content;
    private LocalDateTime timestamp;
}
