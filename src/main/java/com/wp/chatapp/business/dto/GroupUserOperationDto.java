package com.wp.chatapp.business.dto;

import com.wp.chatapp.business.enums.GroupOperationType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GroupUserOperationDto {
    private GroupOperationType operationType;
    private List<String> userIds;
}
