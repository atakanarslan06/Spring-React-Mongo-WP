package com.wp.chatapp.business.controllers;

import com.wp.chatapp.business.dto.GroupMessageDto;
import com.wp.chatapp.business.services.GroupMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/group-messages")
@RequiredArgsConstructor
public class GroupMessageController {
    private final GroupMessageService groupMessageService;

    @GetMapping("/{groupId}")
    public ResponseEntity<List<GroupMessageDto>> getGroupMessagesByGroupId(@PathVariable String groupId){
        List<GroupMessageDto> groupMessages = groupMessageService.getGroupMessagesByGroupId(groupId);
        return ResponseEntity.ok(groupMessages);
    }

    @PostMapping
    public ResponseEntity<String> sendGroupMessage(@RequestBody GroupMessageDto groupMessageDto){
        groupMessageService.sendGroupMessage(groupMessageDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Message sent successfully");
    }
    @PatchMapping("/{messageId}")
    public ResponseEntity<String> deleteGroupMessage(@PathVariable String messageId){
        groupMessageService.deleteGroupMessage(messageId);
        return ResponseEntity.ok("Message deleted successfully");
    }
}
