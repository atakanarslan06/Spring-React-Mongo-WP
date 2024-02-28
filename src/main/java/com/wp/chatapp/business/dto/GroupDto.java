package com.wp.chatapp.business.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
public class GroupDto {

    private String name;
    private String adminUserId;
    private List<String> members;
    private List<String> userIds;
}
