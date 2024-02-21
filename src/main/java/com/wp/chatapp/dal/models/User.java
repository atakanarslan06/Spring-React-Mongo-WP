package com.wp.chatapp.dal.models;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(value = "users")
@Data
@Builder
@Getter
@Setter
public class User {
    @Id
    private String id;
    @Field(name = "user_name")
    private String userName;
    private String email;
    private String password;
    private String profile_picture;
    private List<String> friends;

}
