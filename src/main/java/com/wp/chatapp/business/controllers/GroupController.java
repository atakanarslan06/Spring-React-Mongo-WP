package com.wp.chatapp.business.controllers;

import com.wp.chatapp.business.dto.GroupDto;
import com.wp.chatapp.business.dto.GroupUserOperationDto;
import com.wp.chatapp.business.services.GroupService;
import com.wp.chatapp.dal.models.Group;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/groups")
public class GroupController {
    private final GroupService groupService;
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }
    @PostMapping
    public ResponseEntity<Map<String, String>> createGroup(@RequestBody GroupDto groupDto) {
        String response = groupService.createGroup(groupDto);
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", response);
        return ResponseEntity.ok(responseBody);
    }

    @GetMapping
    public ResponseEntity<List<Group>> getAllGroups(){
        List<Group> groups = groupService.getAllGroups();
        return ResponseEntity.ok(groups);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateGroup(@PathVariable String id, @RequestBody GroupDto groupDto ){
        String response = groupService.updateGroup(id, groupDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Group> getGroupById(@PathVariable String id){
        Group group = groupService.getGroupById(id);
        return ResponseEntity.ok(group);
    }

    @PostMapping("/{id}")
    public ResponseEntity<String> manageGroupUsers(@PathVariable String id, @RequestBody GroupUserOperationDto dto) {
        String response = groupService.manageGroupUsers(id, dto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<String> deactivateGroup(@PathVariable String id){
        String response = groupService.deactivateGroup(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Group>> getGroupsByUserId(@PathVariable String userId){
        List<Group> groups = groupService.getGroupsByUserId(userId);
        return ResponseEntity.ok(groups);
    }
}
