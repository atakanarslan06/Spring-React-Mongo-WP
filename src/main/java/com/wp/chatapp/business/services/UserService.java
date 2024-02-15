package com.wp.chatapp.business.services;

import com.wp.chatapp.business.dto.UserDto;
import com.wp.chatapp.dal.models.User;
import com.wp.chatapp.dal.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public String createUser(UserDto userDto){
        try
        {
            User user = User.builder()
                    .userName(userDto.getUserName())
                    .email(userDto.getEmail())
                    .password(userDto.getPassword())
                    .build();
            userRepository.save(user);
            return "User Created Successfully";
        }
        catch (Exception e)
        {
        return "User Not Created";
        }
    }
    public String updateUser(String id, UserDto userDto){
        Optional<User> existingUserOptional = userRepository.findById(id);
        if (existingUserOptional.isPresent()){
            User existingUser = existingUserOptional.get();
            existingUser.setUserName(userDto.getUserName());
            existingUser.setEmail(userDto.getEmail());
            existingUser.setPassword(userDto.getPassword());
            userRepository.save(existingUser);
            return "User Updated Successfulyy";
        } else {
            return "User Not Found";
        }
    }
}
