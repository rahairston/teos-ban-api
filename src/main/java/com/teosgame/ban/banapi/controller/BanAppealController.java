package com.teosgame.ban.banapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teosgame.ban.banapi.exception.InvalidTokenException;
import com.teosgame.ban.banapi.model.request.BanAppealRequest;

@RestController
@RequestMapping("/appeal")
public class BanAppealController {
    
    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<Void> getBanAppeals() throws InvalidTokenException {
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/", produces = "application/json")
    public ResponseEntity<Void> creatBanAppeal(@RequestBody @Validated BanAppealRequest request)  {
        System.out.println(request.toString());
        return ResponseEntity.noContent().build();
    }
}
