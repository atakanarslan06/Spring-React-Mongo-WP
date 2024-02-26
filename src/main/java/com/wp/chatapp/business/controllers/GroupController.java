package com.wp.chatapp.business.controllers;

import com.wp.chatapp.business.dto.GroupDto;
import com.wp.chatapp.business.services.GroupService;
import com.wp.chatapp.dal.models.Group;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/groups")
public class GroupController {
    private final GroupService groupService;
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }
    @PostMapping
    public ResponseEntity<String> createGroup(@RequestBody GroupDto groupDto){
        String response = groupService.createGroup(groupDto);
        return ResponseEntity.ok(response);
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

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<String> deactivateGroup(@PathVariable String id){
        String response = groupService.deactivateGroup(id);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/{id}/addUsers")
    public ResponseEntity<String> addUsersToGroup(@PathVariable String id, @RequestBody GroupDto dto){
        String response = groupService.addUsersToGroup(id, dto.getUserIds());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{groupId}/removeUser/{userId}")
    public ResponseEntity<String> removeUserFromGroup(@PathVariable String groupId, @PathVariable String userId) {
        String response = groupService.removeUserFromGroup(groupId, userId);
        return ResponseEntity.ok(response);
    }

}
