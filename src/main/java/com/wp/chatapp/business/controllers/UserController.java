package com.wp.chatapp.business.controllers;

import com.wp.chatapp.business.dto.UserDto;
import com.wp.chatapp.business.services.UserService;
import com.wp.chatapp.dal.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserDto userDto){
        String response = userService.createUser(userDto);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable String id, @RequestBody UserDto userDto){
        String response = userService.updateUser(id, userDto);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id){
        Optional<User> userOptional  = userService.getUserById(id);
        if (userOptional.isPresent()){
            User user = userOptional.get();
            return ResponseEntity.ok(user);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<String> activateUser(@PathVariable String id){
        String response = userService.activateUser(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<String> deactivateUser(@PathVariable String id){
        String response = userService.deactivateUser(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/findByPhoneNumber/{phoneNumber}")
    public ResponseEntity<User> findByPhoneNumber(@PathVariable String phoneNumber){
      User user = userService.findByPhoneNumber(phoneNumber);
      return ResponseEntity.ok(user);
    }

    @PostMapping("/acceptRequest/{userId}/{friendId}")
    public ResponseEntity<String> acceptFriendRequest(@PathVariable String userId, @PathVariable String friendId){
        String response = userService.acceptFriendRequest(userId, friendId);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/rejectRequest/{userId}/{friendId}")
    public ResponseEntity<String> rejectFriendRequest(@PathVariable String userId, @PathVariable String friendId){
        String response = userService.rejectFriendRequest(userId, friendId);
        return ResponseEntity.ok(response);
    }

}
