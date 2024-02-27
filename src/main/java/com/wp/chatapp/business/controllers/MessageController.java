package com.wp.chatapp.business.controllers;

import com.wp.chatapp.business.dto.MessageDto;
import com.wp.chatapp.business.services.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {
    private final MessageService messageService;


    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<MessageDto>> getMessagesByUserId(@PathVariable String userId){
        List<MessageDto> messages = messageService.getMessagesByUserId(userId);
        return ResponseEntity.ok(messages);
    }
    @PostMapping
    public ResponseEntity<String> sendMessage(@RequestBody MessageDto messageDto){
        messageService.sendMessage(messageDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Message sent successfully");
    }

    @GetMapping("/last/{userId}")
    public ResponseEntity<MessageDto> getLastMessageByUserId(@PathVariable String userId){
        MessageDto lastMessage = messageService.getLastMessageByUserId(userId);
        return ResponseEntity.ok(lastMessage);
    }
}
