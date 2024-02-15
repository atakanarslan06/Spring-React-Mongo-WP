package com.wp.chatapp.business.services;

import com.wp.chatapp.business.dto.UserDto;
import com.wp.chatapp.dal.models.User;
import com.wp.chatapp.dal.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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
            return "User Updated Successfully";
        } else {
            return "User Not Found";
        }
    }
    public String deleteUser(String id) {
        try {
            userRepository.deleteById(id);
            return "User Deleted Successfully";
        }catch (Exception e){
            return "User Not Deleted";
        }
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public Optional<User> getUserById(String id){
        return userRepository.findById(id);
    }
    public String addFriend(String userId, String friendId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            return "User Not Found";
        }

        Optional<User> friendOptional = userRepository.findById(friendId);
        if (!friendOptional.isPresent()) {
            return "Friend User Not Found";
        }

        User user = userOptional.get();
        User friend = friendOptional.get();

        if (user.getFriends().contains(friendId)) {
            return "Friend already exists";
        }

        user.getFriends().add(friendId);
        userRepository.save(user);
        return "Friend Added Successfully";
    }

    public String removeFriend(String userId, String friendId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            return "User Not Found";
        }

        User user = userOptional.get();

        if (!user.getFriends().contains(friendId)) {
            return "Friend Not Found";
        }

        user.getFriends().remove(friendId);
        userRepository.save(user);
        return "Friend Removed Successfully";
    }

}
