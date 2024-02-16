package com.wp.chatapp.business.controllers;

import com.wp.chatapp.business.dto.UserDto;
import com.wp.chatapp.business.services.UserService;
import com.wp.chatapp.dal.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody UserDto userDto){
        String response = userService.createUser(userDto);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUser(@PathVariable String id, @RequestBody UserDto userDto){
        String response = userService.updateUser(id, userDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id){
        String response = userService.deleteUser(id);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
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
    @PostMapping("/{userId}/addFriend/{friendId}")
    public ResponseEntity<String> addFriend(@PathVariable String userId, @PathVariable String friendId) {
        String response = userService.addFriend(userId, friendId);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{userId}/removeFriend/{friendId}")
    public ResponseEntity<String> removeFriend(@PathVariable String userId, @PathVariable String friendId ){
        String response = userService.removeFriend(userId, friendId);
        return ResponseEntity.ok(response);
    }
}
