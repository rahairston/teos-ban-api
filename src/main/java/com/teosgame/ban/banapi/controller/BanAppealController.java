package com.teosgame.ban.banapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/appeal")
public class BanAppealController {
    
    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<Void> getAuthToken()  {
        return ResponseEntity.noContent().build();
    }
}
