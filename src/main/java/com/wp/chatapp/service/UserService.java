package com.wp.chatapp.service;

import com.wp.chatapp.model.User;
import com.wp.chatapp.request.UpdateUserRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    public User findUserById(String id);
    public User updateUser(String id, UpdateUserRequest request)
    public List<User> searchUser(String query);

}
