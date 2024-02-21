package com.wp.chatapp.business.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
public class UserDto {
    private String id;
    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private String profile_picture;
    private String role;
}
