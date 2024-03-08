package com.wp.chatapp.business.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class UserDto {
    private String id;
    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private List<String> friends;
    private List<String> friendRequests;
    private String profile_picture;
}
