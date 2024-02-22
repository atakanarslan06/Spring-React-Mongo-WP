package com.wp.chatapp.business.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private String id;
    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private String profile_picture;
}
