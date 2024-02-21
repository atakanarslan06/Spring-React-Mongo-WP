package com.wp.chatapp.business.controllers;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo-controller")
public class demo {
    @GetMapping
    public ResponseEntity<String> sayhello(){
        return ResponseEntity.ok("heelllooo");
    }
}
