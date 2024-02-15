package com.wp.chatapp.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UpdateUserRequest {
    private String userName;
    private String profile_picture;

    public UpdateUserRequest(){

    }
    public UpdateUserRequest(String userName, String profile_picture) {
        this.userName = userName;
        this.profile_picture = profile_picture;
    }
}
