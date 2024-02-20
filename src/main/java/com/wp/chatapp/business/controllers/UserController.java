package com.wp.chatapp.business.controllers;

import com.wp.chatapp.business.dto.UserDto;
import com.wp.chatapp.business.services.UserService;
import com.wp.chatapp.dal.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserDto userDto){
        String response = userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id){
        String response = userService.deleteUser(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id){
        Optional<User> userOptional  = userService.getUserById(id);
        return userOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/activate/{userId}")
    public ResponseEntity<String> patchActive(@PathVariable String userId) {
        String response = userService.activateUser(userId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/deactivate/{userId}")
    public ResponseEntity<String> patchPassive(@PathVariable String userId) {
        String response = userService.deactivateUser(userId);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<String> addFriend(@PathVariable String userId, @PathVariable String friendId) {
        String response = userService.addFriend(userId, friendId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<String> removeFriend(@PathVariable String userId, @PathVariable String friendId ){
        String response = userService.removeFriend(userId, friendId);
        return ResponseEntity.ok(response);
    }
}
