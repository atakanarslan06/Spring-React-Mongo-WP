package com.wp.chatapp.business.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
public class UserDto {
    private String id;
    private String userName;
    private String email;
    private String password;
    private String profile_picture;
}
